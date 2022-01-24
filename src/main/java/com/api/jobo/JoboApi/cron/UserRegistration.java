package com.api.jobo.JoboApi.cron;

import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.model.UserUpdateForm;
import com.api.jobo.JoboApi.api.specification.JobPredicate;
import com.api.jobo.JoboApi.api.specification.ReviewPredicate;
import com.api.jobo.JoboApi.api.specification.UserPredicate;
import com.api.jobo.JoboApi.config.security.AppRolesEnum;
import com.api.jobo.JoboApi.utils.MyLinkedMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.api.jobo.JoboApi.globals.GlobalService.jobService;
import static com.api.jobo.JoboApi.globals.GlobalService.userService;
import static com.api.jobo.JoboApi.utils.DataOps.getObjectMapper;


@Slf4j
@Component
public class UserRegistration {

    @Scheduled(fixedDelay = 5000, initialDelay = 10000) //every 10 seconds
    private void addRolesToUsers() {
        Page<Models.AppUser> usersWithoutRoles = userService.getAllUsers(new UserPredicate(new Models.AppUser()), PageRequest.of(0, Integer.MAX_VALUE));
        if (!usersWithoutRoles.isEmpty()) {
            //log.info("Checking user roles");
            usersWithoutRoles.forEach(u -> {
                if (u.getRole() == null) {
                    Models.AppRole role = userService.getARole(AppRolesEnum.ROLE_CLIENT.name());
                    if (role != null) {
                        try {
                            log.info("Adding client role to " + u.getUsername());
                            userService.addARoleToAUser(u.getUsername(), role.getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @Scheduled(fixedDelay = 5000, initialDelay = 10000) //every 10 seconds
    private void rateUsers() {
        Page<Models.AppUser> allUsers = userService.getAllUsers(new UserPredicate(new Models.AppUser()), PageRequest.of(0, Integer.MAX_VALUE));
        if (!allUsers.isEmpty()) {
            allUsers.forEach(user -> {
                Page<Models.Review> allReviews = jobService.getReviews(new ReviewPredicate(new Models.Review(user.getRole().getName().equals(AppRolesEnum.ROLE_SERVICE_PROVIDER.name()) ? user.getUsername() : null, user.getRole().getName().equals(AppRolesEnum.ROLE_CLIENT.name()) ? user.getUsername() : null)), PageRequest.of(0, Integer.MAX_VALUE));

                if (!allReviews.isEmpty()) {
                    final float[] reviewRating = {0F};
                    final int[] reviewCount = {0};

                    allReviews.forEach(r -> {
                        if (user.getRole().getName().equals(AppRolesEnum.ROLE_SERVICE_PROVIDER.name())) {
                            reviewCount[0] = reviewCount[0] + 1;

                            if (r.getClientReview() != null) {

                                try {
                                    MyLinkedMap clientRatingObj = getObjectMapper().readValue(r.getClientReview(), MyLinkedMap.class);
                                    Map.Entry clientEntry = clientRatingObj.getEntry(0);
                                    Float contextRating = Float.parseFloat(String.valueOf(clientEntry.getKey()));
                                    reviewRating[0] = reviewRating[0] + contextRating;
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                reviewRating[0] = reviewRating[0] + 0;
                            }

                        } else {
                            reviewCount[0] = reviewCount[0] + 1;

                            if (r.getLocalServiceProviderReview() != null) {

                                try {
                                    MyLinkedMap providerRatingObj = getObjectMapper().readValue(r.getLocalServiceProviderReview(), MyLinkedMap.class);
                                    Map.Entry providerEntry = providerRatingObj.getEntry(0);
                                    Float contextRating = Float.parseFloat(String.valueOf(providerEntry.getKey()));
                                    reviewRating[0] = reviewRating[0] + contextRating;
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                reviewRating[0] = reviewRating[0] + 0;
                            }
                        }
                    });

                    try {
                        userService.updateAUser(user.getUsername(), new UserUpdateForm(getAverageRating(reviewRating[0], reviewCount[0])));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }

    private float getAverageRating(float totalRating, int jobs) {
        return totalRating / jobs;
    }

    @Scheduled(fixedDelay = 5000, initialDelay = 10000) //every 10 seconds
    private void remindJobs() {
        Page<Models.Job> allJobs = jobService.getAllJobs(new JobPredicate(new Models.Job()), PageRequest.of(0, Integer.MAX_VALUE));
        //todo remind scheduled jobs


        if (!allJobs.isEmpty()) {

        }
    }


}
