package com.api.jobo.JoboApi.api.model;


import com.api.jobo.JoboApi.api.validation.email.ValidEmail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.*;

import static com.api.jobo.JoboApi.globals.GlobalVariables.*;

@Getter
@Setter
public class NewUserForm {


    @NotBlank(message = "name is required")
    @JsonProperty(value = NAMES)
    private String names;

    @NotBlank(message = "username is required")
    @JsonProperty(value = USERNAME)
    private String username;

    @NotBlank(message = "email is required")
    @JsonProperty(value = EMAIL_ADDRESS)
    @ValidEmail(message = "invalid email")
    private String emailAddress;

    @NotBlank(message = "password is required")
    @JsonProperty(value = PASSWORD)
    private String password;

    @JsonProperty(value = PHONE_NUMBER)
    private String phoneNumber = HY;

    @JsonProperty(value = ID_NUMBER)
    private String idNumber = HY;

    @JsonProperty(value = BIO)
    private String bio = HY;

    @JsonProperty(value = PREFERRED_WORKING_HOURS)
    private LinkedHashMap<String, String> preferredWorkingHours = new LinkedHashMap<>();

    @JsonProperty(value = SPECIALITIES)
    private LinkedList<String> specialities = new LinkedList<>();

    private String role;


    public NewUserForm() {

    }

    public NewUserForm(String names, String username, String emailAddress, String password, String phoneNumber, String idNumber, String bio, LinkedHashMap<String, String> preferredWorkingHours, LinkedList<String> specialities, String role) {
        this.names = names;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.bio = bio;
        this.preferredWorkingHours = preferredWorkingHours;
        this.specialities = specialities;
        this.role = role;
    }
}
