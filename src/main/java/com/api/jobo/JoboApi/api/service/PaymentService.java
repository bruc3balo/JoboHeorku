package com.api.jobo.JoboApi.api.service;


import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.model.CashFlowTime;
import com.api.jobo.JoboApi.api.model.MakeStkRequest;
import com.api.jobo.JoboApi.api.model.ServiceCashFlow;
import com.api.jobo.JoboApi.api.specification.PaymentSpecification;
import io.swagger.models.auth.In;
import javassist.NotFoundException;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface PaymentService {
    String authenticate() throws IOException, JSONException;

    Models.Payment saveNewPayment(MakeStkRequest request) throws JSONException, IOException, ParseException, InterruptedException, NotFoundException;

    Page<Models.Payment> getPayments(PaymentSpecification specification, PageRequest pageRequest);

    List<ServiceCashFlow> getServiceCashFlows();

    List<CashFlowTime> getMonthlyCashFlows(Integer month, Integer year);

    List<CashFlowTime> getYearlyCashFlows(Integer year);

    List<CashFlowTime> getTodaysCashFlows();

}
