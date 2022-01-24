package com.api.jobo.JoboApi.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserActivityModel {
    private Date date;
    private int quantity = 0;

    public UserActivityModel() {

    }
    public UserActivityModel(Date date) {
        this.date = date;
    }

    public void addQuantity () {
        quantity = quantity + 1;
    }

}
