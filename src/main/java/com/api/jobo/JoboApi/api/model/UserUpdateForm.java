package com.api.jobo.JoboApi.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import static com.api.jobo.JoboApi.globals.GlobalVariables.*;

@Getter
@Setter
public class UserUpdateForm {
    @JsonProperty(value = NAMES)
    private String names;

    @JsonProperty(value = EMAIL_ADDRESS)
    private String emailAddress;

    @JsonProperty(value = PASSWORD)
    private String password;

    @JsonProperty(value = ROLE)
    private String role;

    @JsonProperty(value = PHONE_NUMBER)
    private String phoneNumber;

    @JsonProperty(value = ID_NUMBER)
    private String idNumber;

    @JsonProperty(value = BIO)
    private String bio;

    @JsonProperty(value = LAST_LOCATION)
    private String location;

    @JsonProperty(value = PREFERRED_WORKING_HOURS)
    private LinkedHashMap<String, String> preferredWorkingHours;

    @JsonProperty(value = SPECIALITIES)
    private LinkedList<String> specialities;

    @JsonProperty(value = VERIFIED)
    private Boolean verified;

    @JsonProperty(value = DELETED)
    private Boolean deleted;

    @JsonProperty(value = DISABLED)
    private Boolean disabled;

    @JsonProperty(value = "rating")
    private Float rating;

    @JsonProperty(value = "strikes")
    private Integer strikes;

    public UserUpdateForm(Boolean verified) {
        this.verified = verified;
    }

    public UserUpdateForm(Float rating) {
        this.rating = rating;
    }

    public UserUpdateForm() {

    }

    public UserUpdateForm(Integer strikes) {
        this.strikes = strikes;
    }

    public UserUpdateForm(String names, String emailAddress, String password, String role, Boolean verified) {
        this.names = names;
        this.emailAddress = emailAddress;
        this.password = password;
        this.role = role;
        this.verified = verified;
    }

    public UserUpdateForm(String names, String emailAddress, String password, String role, String phoneNumber, String idNumber, String bio, LinkedHashMap<String, String> preferredWorkingHours, LinkedList<String> specialities,Boolean verified) {
        this.names = names;
        this.emailAddress = emailAddress;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.bio = bio;
        this.preferredWorkingHours = preferredWorkingHours;
        this.specialities = specialities;
        this.verified = verified;
    }
}
