package com.api.jobo.JoboApi.globals;

import com.api.jobo.JoboApi.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class GlobalService {

    public static UserService userService;
    public static JobService jobService;
    public static UserDetailsService userDetailsService;
    public static PasswordEncoder passwordEncoder;
    public static ServiceService serviceService;
    public static PaymentService paymentService;
    public static NotificationService notificationService;

    @Autowired
    public void setJobService(JobService jobService) {
        GlobalService.jobService = jobService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        GlobalService.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        GlobalService.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        GlobalService.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setServiceService(ServiceService serviceService) {
        GlobalService.serviceService = serviceService;
    }

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        GlobalService.paymentService = paymentService;
    }

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        GlobalService.notificationService = notificationService;
    }

}
