package com.api.jobo.JoboApi.api.controller;


import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.model.CashFlowTime;
import com.api.jobo.JoboApi.api.model.MakeStkRequest;
import com.api.jobo.JoboApi.api.model.ServiceCashFlow;
import com.api.jobo.JoboApi.api.specification.PaymentSpecification;
import com.api.jobo.JoboApi.utils.ApiCode;
import com.api.jobo.JoboApi.utils.JsonResponse;
import com.api.jobo.JoboApi.utils.JsonSetErrorResponse;
import com.api.jobo.JoboApi.utils.JsonSetSuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.api.jobo.JoboApi.globals.GlobalService.paymentService;
import static com.api.jobo.JoboApi.utils.DataOps.filterRequestParams;


@RestController
@RequestMapping(value = "/pay")
@Slf4j
public class PaymentController {

    @PostMapping
    public ResponseEntity<?> pay(@Valid @RequestBody MakeStkRequest request) {
        try {

            Models.Payment payment = paymentService.saveNewPayment(request);
            JsonResponse response = JsonSetSuccessResponse.setResponse(HttpStatus.OK.value(), "Request successful", UUID.randomUUID().toString(), payment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage(), UUID.randomUUID().toString());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getPayments(@RequestParam(name = "mobile_number") String mobileNumber) {
        try {

            Page<Models.Payment> payment = paymentService.getPayments(new PaymentSpecification(mobileNumber, null), PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "id"));

            if (payment.getContent().isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.NOT_FOUND.value(), "Payment not found", UUID.randomUUID().toString());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            JsonResponse response = JsonSetSuccessResponse.setResponse(HttpStatus.OK.value(), "Request successful ( " + payment.getContent().size() + " )", UUID.randomUUID().toString(), payment.getContent());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage(), UUID.randomUUID().toString());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/cashflow"})
    public ResponseEntity<?> getPaymentsCashFlow() {
        try {

            List<ServiceCashFlow> payment = paymentService.getServiceCashFlows();

            JsonResponse response = JsonSetSuccessResponse.setResponse(HttpStatus.OK.value(), "Request successful ( " + payment.size() + " )", UUID.randomUUID().toString(), payment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage(), UUID.randomUUID().toString());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/today"})
    public ResponseEntity<?> getPaymentsToday() {
        try {

            List<CashFlowTime> payment = paymentService.getTodaysCashFlows();

            if (payment.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.NOT_FOUND.value(), "Payment not found", UUID.randomUUID().toString());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            JsonResponse response = JsonSetSuccessResponse.setResponse(HttpStatus.OK.value(), "Request successful ( " + payment.size() + " )", UUID.randomUUID().toString(), payment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage(), UUID.randomUUID().toString());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/month"})
    public ResponseEntity<?> getPaymentsForMonth(HttpServletRequest request, @RequestParam(name = "month") Integer month, @RequestParam(name = "year") Integer year) {
        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("month", "year"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            List<CashFlowTime> payment = paymentService.getMonthlyCashFlows(month, year);

            if (payment.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.NOT_FOUND.value(), "Payment not found", UUID.randomUUID().toString());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            JsonResponse response = JsonSetSuccessResponse.setResponse(HttpStatus.OK.value(), "Request successful ( " + payment.size() + " )", UUID.randomUUID().toString(), payment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage(), UUID.randomUUID().toString());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/year"})
    public ResponseEntity<?> getPaymentsForMonth(HttpServletRequest request, @RequestParam(name = "year") Integer year) {
        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("year"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            List<CashFlowTime> payment = paymentService.getYearlyCashFlows(year);

            if (payment.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.NOT_FOUND.value(), "Payment not found", UUID.randomUUID().toString());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            JsonResponse response = JsonSetSuccessResponse.setResponse(HttpStatus.OK.value(), "Request successful ( " + payment.size() + " )", UUID.randomUUID().toString(), payment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage(), UUID.randomUUID().toString());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
