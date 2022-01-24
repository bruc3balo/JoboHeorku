package com.api.jobo.JoboApi.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static com.api.jobo.JoboApi.globals.GlobalVariables.PASSWORD;
import static com.api.jobo.JoboApi.globals.GlobalVariables.USERNAME;

@Getter
@Setter
public class UsernameAndPasswordAuthenticationRequest {

    @JsonProperty(USERNAME)
    private String username;

    @JsonProperty(PASSWORD)
    private String password;

    public UsernameAndPasswordAuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UsernameAndPasswordAuthenticationRequest() {
    }
}
