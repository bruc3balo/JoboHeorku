package com.api.jobo.JoboApi.cron;


import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.domain.MpesaModels;
import com.api.jobo.JoboApi.api.model.JobUpdateForm;
import com.api.jobo.JoboApi.api.specification.PaymentSpecification;
import com.api.jobo.JoboApi.utils.JobStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.api.jobo.JoboApi.globals.GlobalRepo.*;
import static com.api.jobo.JoboApi.globals.GlobalService.jobService;
import static com.api.jobo.JoboApi.globals.GlobalService.paymentService;
import static com.api.jobo.JoboApi.globals.GlobalVariables.PAID_RESPONSE;
import static com.api.jobo.JoboApi.utils.DataOps.getObjectMapper;

@Slf4j
@Component
@Transactional
public class ProcessPayment {

    int count = 0;

    @Scheduled(fixedDelay = 5000, initialDelay = 20000) //every 10 seconds
    private void sendStk() {

        List<MpesaModels.MpesaStkCallback> callbacks = stkCallbackFirebaseRepo.retrieveAll();
        System.out.println("callback are " + callbacks.size());

        for (MpesaModels.MpesaStkCallback c : callbacks) {

            if (c.getResultCode() == 17) {
                stkCallbackFirebaseRepo.remove(String.valueOf(c.getId()));
                continue;
            }

            try {
                log.info(count + ". " + getObjectMapper().writeValueAsString(c));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            log.info(count + ". Call back is " + c.getId());
            count++;

            Page<Models.Payment> payments = paymentService.getPayments(new PaymentSpecification(null, c.getCheckoutRequestID()), PageRequest.of(0, Integer.MAX_VALUE));
            if (!payments.isEmpty()) {

                Models.Payment payment = payments.getContent().get(0);

                if (c.getCallbackMetadata() != null) {
                    if (c.getCallbackMetadata().getMpesaCallbackMetadataItems() != null && !c.getCallbackMetadata().getMpesaCallbackMetadataItems().isEmpty()) {
                        //save items
                        //save metadata with items

                        List<MpesaModels.MpesaCallbackMetadataItems> callbackMetadataItemsRepos = c.getCallbackMetadata().getMpesaCallbackMetadataItems();
                        callbackMetadataItemsRepos.forEach(i -> {
                            if (i.getName().equals("Amount")) {
                                payment.setAmount(i.getValue());
                            } else if (i.getName().equals("MpesaReceiptNumber")) {
                                payment.setMpesaReceiptNumber(i.getValue());
                            }
                        });
                        payment.setResultCode(c.getResultCode());

                    }
                }

                payment.setPaid(String.valueOf(c.getResultCode()).equals(PAID_RESPONSE));
                Models.Payment savedPayment = paymentRepo.save(payment);
                log.info("SAVED CALLBACK TO PAYMENT");
                //will save auto

                //get job
                Optional<Models.Job> optionalJob = jobService.getJob(payment.getJobId());
                if (optionalJob.isPresent()) {
                    Models.Job job = optionalJob.get();

                    double[] paid = {0};

                    job.getPayments().stream().filter(i -> i.getAmount() != null).map(i -> Double.parseDouble(i.getAmount())).forEach(amount -> {
                        paid[0] = paid[0] + amount;
                        System.out.println("Adding " + amount);
                    });

                    //add current amount
                    paid[0] = Double.parseDouble(savedPayment.getAmount()) + paid[0];

                    System.out.println("added current "+savedPayment.getAmount());

                    if (job.getJobPrice().doubleValue() <= paid[0]) {
                        try {
                            jobService.updateJob(job.getId(), new JobUpdateForm(JobStatus.RATING.getCode(), savedPayment));
                            log.info("SAVED PAYMENT TO JOB");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Failed on pricing " + job.getJobPrice().doubleValue() + " vs " + paid[0]);
                    }

                    //delete callback
                    System.out.println("Id to remove is " + c.getId());
                    if (stkCallbackFirebaseRepo.remove(String.valueOf(c.getId()))) {
                        log.info("CALLBACK REMOVED");
                    } else {
                        log.error("CALLBACK NOT REMOVED");
                    }
                }
            }
        }

    }
}
