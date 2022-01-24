package com.api.jobo.JoboApi.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryData {

   private int quantity;
   private String title;


    public SummaryData(String title,int quantity) {
        this.quantity = quantity;
        this.title = title;
    }

    public SummaryData() {

    }
}
