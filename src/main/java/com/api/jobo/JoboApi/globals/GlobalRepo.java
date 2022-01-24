package com.api.jobo.JoboApi.globals;

import com.api.jobo.JoboApi.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalRepo {

    public static UserRepo userRepo;
    public static AppRoleRepo appRoleRepo;
    public static ServiceRepo serviceRepo;
    public static ReviewRepo reviewRepo;
    public static JobRepo jobRepo;
    public static PermissionsRepo permissionsRepo;
    public static FeedbackRepo feedbackRepo;
    public static NotificationRepository notificationRepo;

    //payment
    public static PaymentRepo paymentRepo;
    public static StkCallbackFirebaseRepo stkCallbackFirebaseRepo;

    @Autowired
    public void setFeedbackRepo(FeedbackRepo feedbackRepo) {
        GlobalRepo.feedbackRepo = feedbackRepo;
    }

    @Autowired
    public void setUserRepo(StkCallbackFirebaseRepo stkCallbackFirebaseRepo) {
        GlobalRepo.stkCallbackFirebaseRepo = stkCallbackFirebaseRepo;
    }


    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        GlobalRepo.userRepo = userRepo;
    }

    @Autowired
    public void setAppRoleRepo(AppRoleRepo appRoleRepo) {
        GlobalRepo.appRoleRepo = appRoleRepo;
    }

    @Autowired
    public void setServiceRepo(ServiceRepo serviceRepo) {
        GlobalRepo.serviceRepo = serviceRepo;
    }

    @Autowired
    public void setReviewRepo(ReviewRepo reviewRepo) {
        GlobalRepo.reviewRepo = reviewRepo;
    }

    @Autowired
    public void setPermissionsRepo(PermissionsRepo permissionsRepo) {
        GlobalRepo.permissionsRepo = permissionsRepo;
    }

    @Autowired
    public void setJobRepo(JobRepo jobRepo) {
        GlobalRepo.jobRepo = jobRepo;
    }

    @Autowired
    public void setPaymentRepo(PaymentRepo paymentRepo) {
        GlobalRepo.paymentRepo = paymentRepo;
    }


    @Autowired
    public void setStkCallbackFirebaseRepo(StkCallbackFirebaseRepo stkCallbackFirebaseRepo) {
        GlobalRepo.stkCallbackFirebaseRepo = stkCallbackFirebaseRepo;
    }

    @Autowired
    public void setNotificationRepo(NotificationRepository notificationRepo) {
        GlobalRepo.notificationRepo = notificationRepo;
    }

}
