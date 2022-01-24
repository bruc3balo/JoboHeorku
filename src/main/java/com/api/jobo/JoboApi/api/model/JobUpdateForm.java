package com.api.jobo.JoboApi.api.model;

import com.api.jobo.JoboApi.api.domain.Models;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static com.api.jobo.JoboApi.globals.GlobalVariables.*;

@Getter
@Setter
public class JobUpdateForm {

    @JsonProperty(value = JOB_LOCATION)
    private String jobLocation;

    @JsonProperty(value = SCHEDULED_AT)
    private String scheduledAt;

    @JsonProperty(value = COMPLETED_AT)
    private Boolean completedAt;

    @JsonProperty(value = JOB_PRICE_RANGE)
    private String jobPriceRange;

    @JsonProperty(value = JOB_PRICE)
    private String jobPrice;

    @JsonProperty(value = JOB_STATUS)
    private Integer jobStatus;

    @JsonProperty(value = REPORTED)
    private Boolean reported;

    @JsonProperty(value = PAYMENT)
    private Models.Payment payment;

    public JobUpdateForm() {

    }

    public JobUpdateForm(Integer jobStatus,Models.Payment payment) {
        this.jobStatus = jobStatus;
        this.payment = payment;
    }
}
