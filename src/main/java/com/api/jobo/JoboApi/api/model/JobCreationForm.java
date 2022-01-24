package com.api.jobo.JoboApi.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static com.api.jobo.JoboApi.globals.GlobalVariables.*;

@Getter
@Setter
public class JobCreationForm {

    @JsonProperty(value = LOCAL_SERVICE_PROVIDER_USERNAME)
    private String localServiceProviderUsername;

    @JsonProperty(value = CLIENT_USERNAME)
    private String clientUsername;

    @JsonProperty(value = JOB_LOCATION)
    private String jobLocation;

    @JsonProperty(SPECIALITIES)
    private String specialities;

    @JsonProperty(value = SCHEDULED_AT)
    private String scheduledAt;

    @JsonProperty(value = JOB_DESCRIPTION)
    private String jobDescription;

    @JsonProperty(value = JOB_PRICE_RANGE)
    private String jobPriceRange;

    public JobCreationForm() {

    }

    public JobCreationForm(String localServiceProviderUsername, String clientUsername, String jobLocation, String specialities, String scheduledAt, String jobDescription,String jobPriceRange) {
        this.localServiceProviderUsername = localServiceProviderUsername;
        this.clientUsername = clientUsername;
        this.jobLocation = jobLocation;
        this.specialities = specialities;
        this.scheduledAt = scheduledAt;
        this.jobDescription = jobDescription;
        this.jobPriceRange = jobPriceRange;
    }

}
