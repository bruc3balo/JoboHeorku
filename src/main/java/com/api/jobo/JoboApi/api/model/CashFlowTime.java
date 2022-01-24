package com.api.jobo.JoboApi.api.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CashFlowTime {

    private Date date;

    private BigDecimal quantity;

    public CashFlowTime(Date date, BigDecimal quantity) {
        this.date = date;
        this.quantity = quantity;
    }
}
