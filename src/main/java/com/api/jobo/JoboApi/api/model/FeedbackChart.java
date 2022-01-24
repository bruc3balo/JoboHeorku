package com.api.jobo.JoboApi.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackChart {

    @JsonProperty("rating")
    private int rating;

    @JsonProperty("amount")
    private int amount;

    public FeedbackChart(int rating, Integer amount) {
        this.rating = rating;
        this.amount = amount;
    }

    public FeedbackChart(int rating) {
        this.rating = rating;
    }

    public void addAmount() {
        amount = 1 + amount;
    }
}
