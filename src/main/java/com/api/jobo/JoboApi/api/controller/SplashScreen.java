package com.api.jobo.JoboApi.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class SplashScreen {

    @PostMapping("splashscreen")
    public ResponseEntity<?> splashScreen() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
