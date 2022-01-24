package com.api.jobo.JoboApi.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MakeStkRequest {

    @JsonProperty("transaction_type")
    @NotBlank(message = "transaction type missing")
    private String transactionType;

    @JsonProperty("amount")
    @NotBlank(message = "amount missing")
    private String amount;

    @JsonProperty("phone_number")
    @NotBlank(message = "phone number missing")
    private String phoneNumber;

    @JsonProperty("party_a")
    @NotBlank(message = "party a i.e.phone number missing")
    private String partyA;

    @JsonProperty("job_id")
    @NotNull(message = "job id missing")
    private Long jobId;

    @JsonProperty("transaction_desc")
    private String transactionDesc = "-";


    public MakeStkRequest() {

    }

    public MakeStkRequest(String transactionType, String amount, String phoneNumber, String partyA, Long jobId, String transactionDesc) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.phoneNumber = phoneNumber;
        this.partyA = partyA;
        this.jobId = jobId;
        this.transactionDesc = transactionDesc;
    }
}
