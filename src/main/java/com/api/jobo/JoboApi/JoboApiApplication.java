package com.api.jobo.JoboApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

import static com.api.jobo.JoboApi.globals.GlobalService.userService;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
@Slf4j
@CrossOrigin("*")
public class JoboApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JoboApiApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            //userService.defaults();
        };

    }
}
