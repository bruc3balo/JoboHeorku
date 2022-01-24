package com.api.jobo.JoboApi.config.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

import static com.api.jobo.JoboApi.globals.GlobalVariables.secretJwt;


@Configuration
public class JwtSecretKey {
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretJwt.getBytes());
    }
}
