package com.api.jobo.JoboApi.api.model;

import com.api.jobo.JoboApi.api.domain.Models;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ServiceCashFlow {

    @JsonProperty("service")
    private Models.Service service;

    @JsonProperty("amount")
    private BigDecimal amount;

    public ServiceCashFlow(Models.Service service, BigDecimal amount) {
        this.service = service;
        this.amount = amount;
    }

    public ServiceCashFlow(Models.Service service) {
        this.service = service;
        this.amount = new BigDecimal(0);
    }

    public void addAmount(BigDecimal bigDecimal) {
        if (amount == null) {
            amount = new BigDecimal(0);
        }
        amount = bigDecimal.add(amount);
    }
}
