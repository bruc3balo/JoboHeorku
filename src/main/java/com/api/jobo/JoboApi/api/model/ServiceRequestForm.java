package com.api.jobo.JoboApi.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.api.jobo.JoboApi.globals.GlobalVariables.DESCRIPTION;
import static com.api.jobo.JoboApi.globals.GlobalVariables.NAME;

@Getter
@Setter
public class ServiceRequestForm {
    @JsonProperty(value = DESCRIPTION)
    @NotBlank(message = "Description missing")
    private String description;

    @JsonProperty(value = NAME)
    @NotBlank(message = "Name missing")
    private String name;

    public ServiceRequestForm() {

    }

    public ServiceRequestForm(String name, String description) {
        this.description = description;
        this.name = name;
    }
}
