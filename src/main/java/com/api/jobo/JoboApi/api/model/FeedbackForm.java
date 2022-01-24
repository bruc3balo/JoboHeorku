package com.api.jobo.JoboApi.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.api.jobo.JoboApi.globals.GlobalVariables.USERNAME;

@Getter
@Setter
public class FeedbackForm {

    @JsonProperty(USERNAME)
    @NotBlank(message = "username missing")
    private String username;

    @JsonProperty("comment")
    @NotBlank(message = "comment missing")
    private String comment;

    @JsonProperty("rating")
    @NotNull(message = "rating missing")
    private Integer rating;

    public FeedbackForm() {
    }
}
