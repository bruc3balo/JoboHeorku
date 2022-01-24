package com.api.jobo.JoboApi.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static com.api.jobo.JoboApi.globals.GlobalVariables.*;

@Getter
@Setter
public class ServiceUpdateForm {

    @JsonProperty(value = NAME)
    private String name;

    @JsonProperty(value = DISABLED)
    private Boolean disabled;

    @JsonProperty(value = DESCRIPTION)
    private String description;

    public ServiceUpdateForm() {

    }

    public ServiceUpdateForm(String name, Boolean disabled, String description) {
        this.name = name;
        this.disabled = disabled;
        this.description = description;
    }

}
