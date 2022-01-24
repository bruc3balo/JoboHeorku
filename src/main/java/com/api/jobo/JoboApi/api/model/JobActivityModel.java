package com.api.jobo.JoboApi.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobActivityModel {
    private int hour;
    private int quantity = 0;

    public JobActivityModel() {

    }

    public JobActivityModel(int hour) {
        this.hour = hour;
    }

    public void addQuantity () {
        quantity = quantity + 1;
    }

}
