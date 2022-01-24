package com.api.jobo.JoboApi.api.service;


import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.domain.Models.Payment;
import com.api.jobo.JoboApi.api.domain.MpesaModels;
import com.api.jobo.JoboApi.api.model.CashFlowTime;
import com.api.jobo.JoboApi.api.model.MakeStkRequest;
import com.api.jobo.JoboApi.api.model.ServiceCashFlow;
import com.api.jobo.JoboApi.api.specification.PaymentSpecification;
import com.api.jobo.JoboApi.utils.ConvertToJson;
import com.api.jobo.JoboApi.utils.DataOps;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import static com.api.jobo.JoboApi.globals.GlobalRepo.*;
import static com.api.jobo.JoboApi.globals.GlobalService.jobService;
import static com.api.jobo.JoboApi.globals.GlobalVariables.*;
import static com.api.jobo.JoboApi.utils.DataOps.*;
import static org.apache.tomcat.util.http.fileupload.FileUploadBase.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {

    static String token;

    @Override
    public Payment saveNewPayment(MakeStkRequest request) throws JSONException, IOException, ParseException, InterruptedException, NotFoundException {

        if (jobService.getJob(request.getJobId()).isEmpty()) {
            throw new NotFoundException("Job not found");
        }

        MpesaModels.STKPushResponseBody responseBody = sendStkPush(request).orElseThrow();
        String transactionDescription = request.getTransactionDesc();
        String narration = responseBody.getResponseDescription();
        String phoneNumber = request.getPhoneNumber();
        Long jobId = request.getJobId();
        Date confirmedAt = DataOps.getNowFormattedFullDate();
        Date createdAt = DataOps.getNowFormattedFullDate();
        String accessToken = token;

        //todo make repositories for mpesa models
        Payment savedPayment = paymentRepo.save(new Payment(responseBody, narration, transactionDescription, phoneNumber, jobId, confirmedAt, accessToken, createdAt));
        Thread.sleep(2000);

        return paymentRepo.save(savedPayment);
    }

    @Override
    public String authenticate() throws IOException, JSONException {

        String encodedBasicAuthString = getBasicAuth(appKey, appSecret);
        System.out.println("Authorization " + encodedBasicAuthString);


        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .method("GET", null)
                .addHeader("Authorization", "Basic " + encodedBasicAuthString)
                .build();
        Response response = client.newCall(request).execute();


        assert response.body() != null;
        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
        MpesaModels.MpesaAuthResponse mpesaAuthResponse = getObjectMapper().readValue(jsonObject.toString(), MpesaModels.MpesaAuthResponse.class);


        System.out.println("ACCESS TOKEN ::: " + mpesaAuthResponse.getAccessToken());
        token = mpesaAuthResponse.getAccessToken();
        return mpesaAuthResponse.getAccessToken();
    }

    public Optional<MpesaModels.STKPushResponseBody> sendStkPush(MakeStkRequest request) throws IOException, JSONException {

        JSONArray jsonArray = new JSONArray();


        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put(BusinessShortCode, businessShortCode);
        requestBodyJson.put(Password, getEncodedPassword(businessShortCode, passKey));
        requestBodyJson.put(Timestamp, getTimestamp());
        requestBodyJson.put(TransactionType, transactionType);
        requestBodyJson.put(Amount, request.getAmount());
        requestBodyJson.put(PartyA, request.getPartyA());
        requestBodyJson.put(PartyB, businessShortCode);
        requestBodyJson.put(PhoneNumber, request.getPhoneNumber());
        requestBodyJson.put(CallBackURL, callbackUrl);
        requestBodyJson.put(AccountReference, request.getJobId());
        requestBodyJson.put(TransactionDesc, request.getTransactionDesc());


        jsonArray.put(requestBodyJson);

        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse(APPLICATION_JSON_VALUE);
        RequestBody body = RequestBody.create(mediaType, requestJson);
        Request requesting = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
                .post(body)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .addHeader("Authorization", "Bearer " + authenticate())
                .build();


        Response response = client.newCall(requesting).execute();

        assert response.body() != null;
        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
        MpesaModels.STKPushResponseBody stkPushResponseBody = getObjectMapper().readValue(jsonObject.toString(), MpesaModels.STKPushResponseBody.class);
        System.out.println("response body " + ConvertToJson.setJsonString(stkPushResponseBody));


        return stkPushResponseBody.getResponseCode() != null ? Optional.of(stkPushResponseBody) : Optional.empty();
    }

    @Override
    public Page<Payment> getPayments(PaymentSpecification specification, PageRequest pageRequest) {
        return paymentRepo.findAll(specification, pageRequest);
    }

    @Override
    public List<ServiceCashFlow> getServiceCashFlows() {

        List<ServiceCashFlow> cashFlowList = new ArrayList<>();
        serviceRepo.findAll().forEach(s -> cashFlowList.add(new ServiceCashFlow(s)));

        List<Models.Job> jobList = jobRepo.findAll();

        jobList.forEach(j -> {
            Set<Models.Payment> paymentList = j.getPayments();
            paymentList.forEach(p -> {
                if (p.isPaid()) {
                    List<ServiceCashFlow> serviceCashFlowStream = cashFlowList.stream().filter(i -> i.getService().getName().equals(j.getSpecialities())).collect(Collectors.toList());
                    if (!serviceCashFlowStream.isEmpty()) {
                        ServiceCashFlow serviceCashFlow = serviceCashFlowStream.get(0);
                        serviceCashFlow.addAmount(new BigDecimal(p.getAmount()));
                    }
                }
            });

        });

        return cashFlowList;
    }

    @Override
    public List<CashFlowTime> getTodaysCashFlows() {
        Calendar today = Calendar.getInstance();

        Calendar startToday = Calendar.getInstance();
        startToday.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 0, 0);

        Calendar endToday = Calendar.getInstance();
        endToday.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 23, 59);

        return paymentRepo.findAll().stream().filter(payment -> payment.getCreatedAt().after(startToday.getTime()) && payment.getCreatedAt().before(endToday.getTime()) && payment.isPaid()).map(payment -> new CashFlowTime(payment.getCreatedAt(), new BigDecimal(payment.getAmount()))).collect(Collectors.toList());
    }

    @Override
    public List<CashFlowTime> getMonthlyCashFlows(Integer month, Integer year) {

        Calendar today = Calendar.getInstance();

        Calendar startMonth = Calendar.getInstance();
        startMonth.set(year != null ? year : today.get(Calendar.YEAR), month, 0, 0, 0);

        Calendar endMonth = Calendar.getInstance();
        endMonth.set(year != null ? year : today.get(Calendar.YEAR), month, getLastDayofMonth(month, year != null ? year : endMonth.get(Calendar.YEAR)), 23, 59);

        return paymentRepo.findAll().stream().filter(payment -> payment.getCreatedAt().before(endMonth.getTime()) && payment.getCreatedAt().after(startMonth.getTime()) && payment.isPaid()).map(payment -> new CashFlowTime(payment.getCreatedAt(), new BigDecimal(payment.getAmount()))).collect(Collectors.toList());
    }

    @Override
    public List<CashFlowTime> getYearlyCashFlows(Integer year) {

        Calendar startYear = Calendar.getInstance();
        startYear.set(year, Calendar.JANUARY, 0, 0, 0);

        Calendar endYear = Calendar.getInstance();
        endYear.set(year, Calendar.DECEMBER, getLastDayofMonth(Calendar.DECEMBER, year), 23, 59);

        return paymentRepo.findAll().stream().filter(payment -> payment.getCreatedAt().before(endYear.getTime()) && payment.getCreatedAt().after(startYear.getTime()) && payment.isPaid()).map(payment -> new CashFlowTime(payment.getCreatedAt(), new BigDecimal(payment.getAmount()))).collect(Collectors.toList());
    }

    private int getLastDayofMonth(int month, int year) {

        if (month == 1) {
            //todo leap year
            return isLeapYear(year) ? 29 : 28;
        } else if (isEven(month)) {
            return 30;
        } else {
            return 31;
        }
    }

    public static boolean isLeapYear(int year) {
        // flag to take a non-leap year by default
        boolean is_leap_year = false;

        // If year is divisible by 4
        if (year % 4 == 0) {

            // To identify whether it
            // is a century year or
            // not
            if (year % 100 == 0) {

                // Checking if year is divisible by 400
                // therefore century leap year
                if (year % 400 == 0) {
                    is_leap_year = true;
                } else {
                    is_leap_year = false;
                }
            }

            // Out of if loop that is Non century year
            // but divisible by 4, therefore leap year
            is_leap_year = true;
        }

        // We land here when corresponding if fails
        // If year is not divisible by 4
        else

            // Flag dealing-  Non leap-year
            is_leap_year = false;

        if (!is_leap_year) {
            System.out.println(year + " : Non Leap-year");
        } else {
            System.out.println(year + " : Leap-year");
        }

        return is_leap_year;
    }

    private boolean isEven(int n) {
        return n % 2 == 0;
    }
}
