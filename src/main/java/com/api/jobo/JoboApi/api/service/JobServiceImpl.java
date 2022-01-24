package com.api.jobo.JoboApi.api.service;

import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.domain.Models.NotificationModels;
import com.api.jobo.JoboApi.api.model.*;
import com.api.jobo.JoboApi.api.specification.FeedbackPredicate;
import com.api.jobo.JoboApi.api.specification.ReviewPredicate;
import com.api.jobo.JoboApi.config.security.AppRolesEnum;
import com.api.jobo.JoboApi.utils.DataOps;
import com.api.jobo.JoboApi.utils.JobStatus;
import com.api.jobo.JoboApi.utils.MyLinkedMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.api.jobo.JoboApi.globals.GlobalRepo.*;
import static com.api.jobo.JoboApi.globals.GlobalService.notificationService;
import static com.api.jobo.JoboApi.globals.GlobalService.userService;
import static com.api.jobo.JoboApi.globals.GlobalVariables.*;
import static com.api.jobo.JoboApi.utils.DataOps.*;

@Service
@Transactional
public class JobServiceImpl implements JobService {

    List<Integer> pendingStatus = Arrays.stream(JobStatus.values()).filter(s -> s.code == JobStatus.REQUESTED.code || s.code == JobStatus.NEGOTIATING.code).map(JobStatus::getCode).collect(Collectors.toList());
    List<Integer> failedStatus = Arrays.stream(JobStatus.values()).filter(s -> s.code == JobStatus.DECLINED.code || s.code == JobStatus.CANCELLED.code || s.code == JobStatus.CLIENT_CANCELLED_IN_PROGRESS.code || s.code == JobStatus.SERVICE_CANCELLED_IN_PROGRESS.code || s.code == JobStatus.SERVICE_REPORTED.code || s.code == JobStatus.CLIENT_REPORTED.code).map(JobStatus::getCode).collect(Collectors.toList());
    List<Integer> completedStatus = Arrays.stream(JobStatus.values()).filter(s -> s.code == JobStatus.COMPLETED.code || s.code == JobStatus.CLIENT_COMPLETE.code || s.code == JobStatus.SERVICE_COMPLETE.code || s.code == JobStatus.RATING.code).map(JobStatus::getCode).collect(Collectors.toList());
    List<Integer> ongoingStatus = Arrays.stream(JobStatus.values()).filter(s -> s.code == JobStatus.PROGRESS.code || s.code == JobStatus.SERVICE_COMPLETE.code || s.code == JobStatus.CLIENT_COMPLETE.code || s.code == JobStatus.READY.code || s.code == JobStatus.PAYING.code || s.code == JobStatus.ACCEPTED.code).map(JobStatus::getCode).collect(Collectors.toList());


    @Override
    public Models.Job saveNewJob(JobCreationForm form) throws ParseException, NotFoundException {

        Optional<Models.AppUser> clientOptional = userRepo.findByUsername(form.getClientUsername());
        if (clientOptional.isEmpty()) {
            throw new NotFoundException("Client not found");
        }

        Optional<Models.AppUser> serviceOptional = userRepo.findByUsername(form.getLocalServiceProviderUsername());
        if (serviceOptional.isEmpty()) {
            throw new NotFoundException("Provider not found");
        }

        Models.Job newJob = new Models.Job(form.getLocalServiceProviderUsername(), form.getClientUsername(), form.getJobLocation(), form.getSpecialities(), DataOps.getNowFormattedFullDate(), DataOps.getNowFormattedFullDate(), form.getScheduledAt(), form.getJobPriceRange(), new BigDecimal(0), form.getJobDescription(), 0, false);

        //JobStatus.REQUESTED

        notificationService.postNotification(new NotificationModels("Job has been posted to " + serviceOptional.get().getUsername(), "Job posted successfully", "", clientOptional.get().getUsername(), JOB_COLLECTION));
        notificationService.postNotification(new NotificationModels("You have received a new job request from " + clientOptional.get().getUsername(), newJob.getSpecialities(), newJob.getScheduledAt(), serviceOptional.get().getUsername(), JOB_COLLECTION));

        return jobRepo.save(newJob);
    }

    @Override
    public Optional<Models.Job> getJob(Long jobId) {
        return jobRepo.findById(jobId);
    }

    @Override
    public Page<Models.Job> getAllJobs(Specification<Models.Job> specification, PageRequest pageRequest) {
        return jobRepo.findAll(specification, pageRequest);
    }

    @Override
    public Optional<List<Models.Job>> getAllCreatedJobs(String username) {
        return Optional.of(jobRepo.findAll().stream().filter(i -> Objects.equals(i.getClientUsername(), username)).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<Models.Job>> getAllRequestedJobs(String username) {
        return Optional.of(jobRepo.findAll().stream().filter(i -> Objects.equals(i.getLocalServiceProviderUsername(), username)).collect(Collectors.toList()));
    }

    @Override
    public Models.Job updateJob(Long jobId, JobUpdateForm form) throws Exception {

        Optional<Models.Job> existingJob = getJob(jobId);

        if (existingJob.isEmpty()) {
            throw new NotFoundException("Job not found with id " + jobId);
        }

        Models.Job newJob = existingJob.get();

        Models.AppUser serviceProvider = userService.getAUser(newJob.getLocalServiceProviderUsername());
        Models.AppUser client = userService.getAUser(newJob.getClientUsername());

        if (form != null) {

            if (form.getJobLocation() != null) {
                newJob.setJobLocation(form.getJobLocation());
            }

            if (form.getJobPrice() != null) {
                newJob.setJobPrice(new BigDecimal(form.getJobPrice()));
            }

            if (form.getCompletedAt() != null) {
                newJob.setCompletedAt(DataOps.getNowFormattedFullDate());
            }

            if (form.getScheduledAt() != null) {
                newJob.setScheduledAt(form.getScheduledAt());
            }

            if (form.getJobPriceRange() != null) {
                newJob.setJobPriceRange(form.getJobPriceRange());
                notificationService.postNotification(new NotificationModels("Job for " + newJob.getSpecialities() + " has been suggested to " + form.getJobPriceRange(), "Price suggestion", "Negotiation", serviceProvider.getUsername(), JOB_COLLECTION));
                notificationService.postNotification(new NotificationModels("Job for " + newJob.getSpecialities() + " has been suggested to " + form.getJobPriceRange(), "Price suggestion", "Negotiation", client.getUsername(), JOB_COLLECTION));
            }

            if (form.getPayment() != null) {
                newJob.getPayments().add(form.getPayment());
            }

            if (form.getJobStatus() != null) {
                newJob.setJobStatus(form.getJobStatus());

                switch (form.getJobStatus()) {
                    default:
                        break;

                    // JobStatus.ACCEPTED
                    case 1:
                        notificationService.postNotification(new NotificationModels("Job for " + newJob.getSpecialities() + " has been accepted by " + serviceProvider.getUsername(), "Job accepted successfully", JobStatus.ACCEPTED.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("You have accepted a job for " + client.getUsername(), newJob.getSpecialities(), newJob.getScheduledAt(), serviceProvider.getUsername(), JOB_COLLECTION));
                        break;

                    // JobStatus.DECLINED
                    case 2:
                        notificationService.postNotification(new NotificationModels("Job for " + newJob.getSpecialities() + " has been declined " + serviceProvider.getUsername(), "Job declined", JobStatus.DECLINED.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("You have declined a job for " + client.getUsername(), newJob.getSpecialities(), JobStatus.DECLINED.name(), serviceProvider.getUsername(), JOB_COLLECTION));

                        break;

                    //JobStatus.NEGOTIATING
                    case 3:
                        notificationService.postNotification(new NotificationModels(serviceProvider.getUsername() + " wants to negotiate pricing for job for " + newJob.getSpecialities(), "Negotiation for " + newJob.getSpecialities(), JobStatus.NEGOTIATING.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("Negotiation request has been sent to " + client.getUsername(), newJob.getSpecialities(), JobStatus.NEGOTIATING.name(), serviceProvider.getUsername(), JOB_COLLECTION));


                        break;

                    //JobStatus.READY
                    case 4:
                        notificationService.postNotification(new NotificationModels("Confirmation request sent to " + serviceProvider.getUsername(), "Ready to start job", JobStatus.READY.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels(newJob.getClientUsername() + " is ready to start job", "Ready to start job for " + newJob.getSpecialities(), JobStatus.READY.name(), serviceProvider.getUsername(), JOB_COLLECTION));
                        break;


                    //JobStatus.PROGRESS
                    case 5:
                        notificationService.postNotification(new NotificationModels("Job is in progress for " + newJob.getSpecialities(), JobStatus.PROGRESS.name(), JobStatus.PROGRESS.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("Job is in progress for " + newJob.getSpecialities(), JobStatus.PROGRESS.name(), JobStatus.PROGRESS.name(), serviceProvider.getUsername(), JOB_COLLECTION));

                        break;

                    // JobStatus.COMPLETED
                    case 6:
                        notificationService.postNotification(new NotificationModels("Job has been successfully completed ", JobStatus.COMPLETED.name(), JobStatus.COMPLETED.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("Job has been successfully completed ", JobStatus.COMPLETED.name(), JobStatus.COMPLETED.name(), serviceProvider.getUsername(), JOB_COLLECTION));

                        break;

                    // JobStatus.CANCELLED
                    case 7:
                        notificationService.postNotification(new NotificationModels("Job has been cancelled for " + newJob.getSpecialities(), JobStatus.CANCELLED.name(), JobStatus.CANCELLED.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("Job has been cancelled for " + newJob.getSpecialities(), JobStatus.CANCELLED.name(), JobStatus.CANCELLED.name(), serviceProvider.getUsername(), JOB_COLLECTION));
                        break;

                    // JobStatus.SERVICE_COMPLETE
                    case 8:
                        notificationService.postNotification(new NotificationModels(serviceProvider.getUsername() + " has send completion request for job - " + newJob.getSpecialities(), JobStatus.SERVICE_COMPLETE.name(), JobStatus.SERVICE_COMPLETE.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("Complete request sent to " + client.getUsername(), JobStatus.SERVICE_COMPLETE.name(), JobStatus.SERVICE_COMPLETE.name(), serviceProvider.getUsername(), JOB_COLLECTION));
                        break;

                    // JobStatus.CLIENT_COMPLETE
                    case 9:
                        notificationService.postNotification(new NotificationModels("Complete request sent to " + serviceProvider.getUsername(), JobStatus.SERVICE_COMPLETE.name(), JobStatus.SERVICE_COMPLETE.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels(client.getUsername() + " has send completion request for job - " + newJob.getSpecialities(), JobStatus.CLIENT_COMPLETE.name(), JobStatus.CLIENT_COMPLETE.name(), serviceProvider.getUsername(), JOB_COLLECTION));
                        break;

                    // JobStatus.CLIENT_RATING
                    case 10:
                        notificationService.postNotification(new NotificationModels("You have rated job - " + newJob.getSpecialities(), JobStatus.CLIENT_RATING.name(), JobStatus.CLIENT_RATING.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels(client.getUsername() + " has rated job - " + newJob.getSpecialities(), JobStatus.CLIENT_RATING.name(), JobStatus.CLIENT_RATING.name(), serviceProvider.getUsername(), JOB_COLLECTION));

                        break;

                    // JobStatus.SERVICE_RATING
                    case 11:
                        notificationService.postNotification(new NotificationModels(serviceProvider.getUsername() + " has rated job - " + newJob.getSpecialities(), JobStatus.SERVICE_RATING.name(), JobStatus.SERVICE_RATING.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("You have rated job - " + newJob.getSpecialities(), JobStatus.SERVICE_RATING.name(), JobStatus.SERVICE_RATING.name(), serviceProvider.getUsername(), JOB_COLLECTION));
                        break;

                    //  JobStatus.SERVICE_CANCELLED_IN_PROGRESS
                    case 12:
                        notificationService.postNotification(new NotificationModels(serviceProvider.getUsername() + " has cancelled job - " + newJob.getSpecialities() + " prematurely. You are allowed to report ", JobStatus.SERVICE_CANCELLED_IN_PROGRESS.name(), JobStatus.SERVICE_CANCELLED_IN_PROGRESS.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("You have cancelled job - " + newJob.getSpecialities() + " prematurely", JobStatus.SERVICE_CANCELLED_IN_PROGRESS.name(), JobStatus.SERVICE_CANCELLED_IN_PROGRESS.name(), serviceProvider.getUsername(), JOB_COLLECTION));

                        break;

                    // JobStatus.CLIENT_CANCELLED_IN_PROGRESS
                    case 13:
                        notificationService.postNotification(new NotificationModels("You have cancelled job - " + newJob.getSpecialities() + " prematurely", JobStatus.CLIENT_CANCELLED_IN_PROGRESS.name(), JobStatus.CLIENT_CANCELLED_IN_PROGRESS.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels(client.getUsername() + " has cancelled job - " + newJob.getSpecialities() + " prematurely. You are allowed to report ", JobStatus.CLIENT_CANCELLED_IN_PROGRESS.name(), JobStatus.CLIENT_CANCELLED_IN_PROGRESS.name(), serviceProvider.getUsername(), JOB_COLLECTION));

                        break;

                    // JobStatus.SERVICE_REPORTED
                    case 14:

                        Models.AppUser repostedUser = userService.updateAUser(client.getUsername(), new UserUpdateForm(client.getStrikes() + 1));
                        if (repostedUser.getStrikes() >= 3) {
                            notificationService.postNotification(new NotificationModels("You have " + repostedUser.getStrikes(), "Strike " + repostedUser.getStrikes() + " !!!", "Reported", repostedUser.getUsername(), JOB_COLLECTION));
                            userService.disableUser(repostedUser);
                        }
                        notificationService.postNotification(new NotificationModels("You have been reported for job - " + newJob.getSpecialities(), JobStatus.SERVICE_REPORTED.name(), JobStatus.SERVICE_REPORTED.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("You have reported " + client.getUsername() + " for a job - " + newJob.getSpecialities(), JobStatus.SERVICE_REPORTED.name(), JobStatus.SERVICE_REPORTED.name(), serviceProvider.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("You have " + repostedUser + " strikes . " + (3 - repostedUser.getStrikes()) + " left", JobStatus.SERVICE_REPORTED.name(), JobStatus.SERVICE_REPORTED.name(), client.getUsername(), JOB_COLLECTION));

                        break;

                    // JobStatus.CLIENT_REPORTED
                    case 15:

                        Models.AppUser reportedUser = userService.updateAUser(serviceProvider.getUsername(), new UserUpdateForm(client.getStrikes() + 1));

                        if (reportedUser.getStrikes() >= 3) {
                            notificationService.postNotification(new NotificationModels("You have " + reportedUser.getStrikes(), "Strike " + reportedUser.getStrikes() + " !!!", "Reported", reportedUser.getUsername(), JOB_COLLECTION));
                            userService.disableUser(reportedUser);
                        }

                        notificationService.postNotification(new NotificationModels("You have reported " + serviceProvider.getUsername() + " for a job - " + newJob.getSpecialities(), JobStatus.CLIENT_REPORTED.name(), JobStatus.CLIENT_REPORTED.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("You have been reported for job - " + newJob.getSpecialities(), JobStatus.CLIENT_REPORTED.name(), JobStatus.CLIENT_REPORTED.name(), serviceProvider.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels("You have " + reportedUser + " strikes . " + (3 - reportedUser.getStrikes()) + " left", JobStatus.CLIENT_REPORTED.name(), JobStatus.CLIENT_REPORTED.name(), serviceProvider.getUsername(), JOB_COLLECTION));

                        break;

                    // JobStatus.RATING
                    case 16:
                        notificationService.postNotification(new NotificationModels("You paid for the job - " + newJob.getSpecialities() + " . You can now rate " + serviceProvider.getUsername(), JobStatus.RATING.name(), JobStatus.RATING.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels(client.getUsername() + " has paid for job - " + newJob.getSpecialities(), JobStatus.RATING.name(), JobStatus.RATING.name(), serviceProvider.getUsername(), JOB_COLLECTION));

                        break;

                    // JobStatus.PAYING
                    case 17:
                        notificationService.postNotification(new NotificationModels("You can now pay for the job - " + newJob.getSpecialities(), JobStatus.PAYING.name(), JobStatus.PAYING.name(), client.getUsername(), JOB_COLLECTION));
                        notificationService.postNotification(new NotificationModels(client.getUsername() + " is paying for - " + newJob.getSpecialities(), JobStatus.PAYING.name(), JobStatus.PAYING.name(), serviceProvider.getUsername(), JOB_COLLECTION));

                        break;
                }


            }

            if (form.getReported() != null) {
                newJob.setReported(form.getReported());
            }

            newJob.setUpdatedAt(DataOps.getNowFormattedFullDate());
        }

        return newJob;
    }

    private Optional<JobStatus> getStatusByCode(int code) {
        return Arrays.stream(JobStatus.values()).filter(j -> j.getCode() == code).findFirst();
    }

    @Override
    public boolean deleteJob(Long jobId) throws NotFoundException {
        Optional<Models.Job> existingJob = getJob(jobId);
        if (existingJob.isEmpty()) {
            throw new NotFoundException("Job not found with id " + jobId);
        }
        try {
            jobRepo.delete(existingJob.get());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Models.Review saveReview(Models.Review review) throws DuplicateRequestException, NotFoundException, ParseException, JsonProcessingException {

        if (getJob(review.getJobId()).isEmpty()) {
            throw new NotFoundException("Job not found with id " + review.getJobId());
        }

        if (userRepo.findByUsername(review.getClientUsername()).isEmpty()) {
            throw new NotFoundException("client " + review.getClientUsername() + " not found");
        }

        if (userRepo.findByUsername(review.getLocalServiceProviderUsername()).isEmpty()) {
            throw new NotFoundException("service provider " + review.getLocalServiceProviderUsername() + " not found");
        }

        Optional<Models.Review> existing = findReviewByJobId(review.getJobId());
        if (existing.isPresent()) {
            return updateReview(existing.get());
        }

        review.setUpdatedAt(DataOps.getNowFormattedFullDate().toString());
        review.setCreatedAt(DataOps.getNowFormattedFullDate().toString());

        return reviewRepo.save(review);
    }

    @Override
    public Page<Models.Review> getReviews(Specification<Models.Review> specification, PageRequest pageRequest) {
        return reviewRepo.findAll(specification, pageRequest);
    }

    @Override
    public Models.Review updateReview(Models.Review review) throws NotFoundException, ParseException, JsonProcessingException {

        System.out.println("UPDATING " + new ObjectMapper().writeValueAsString(review));

        if (review.getJobId() == null) {
            throw new NotFoundException("review job id not found");
        }


        Optional<Models.Review> existing = reviewRepo.findById(review.getId());
        if (existing.isEmpty()) {
            throw new NotFoundException("Review not found with id " + review.getJobId());
        }

        Models.Review updated = existing.get();

        if (review.getReported() != null) {
            updated.setReported(true);
        }
        if (review.getClientReview() != null) {
            updated.setClientReview(review.getClientReview());
        }
        if (review.getLocalServiceProviderReview() != null) {
            updated.setLocalServiceProviderReview(review.getLocalServiceProviderReview());
        }

        updated.setUpdatedAt(DataOps.getNowFormattedFullDate().toString());
        return reviewRepo.save(updated);
    }

    private Optional<Models.Review> findReviewByJobId(Long jobId) {
        return reviewRepo.findOne(new ReviewPredicate(new Models.Review(jobId)));
    }

    @Override
    public HashMap<String, Double> getUserReview(String username) throws NotFoundException {

        if (username.isEmpty() || username.isBlank()) {
            throw new NullPointerException();
        }

        Models.AppUser user = userService.getAUser(username);
        if (user == null) {
            throw new NotFoundException("User not found with name " + username);
        }

        final double[] rating = {0.0};
        HashMap<String, Double> review = new HashMap<>();

        review.put(username, rating[0]);


        Page<Models.Review> reviews = reviewRepo.findAll(new ReviewPredicate(user.getRole().getName().equals(AppRolesEnum.ROLE_CLIENT.name()) ? username : null, user.getRole().getName().equals(AppRolesEnum.ROLE_SERVICE_PROVIDER.name()) ? username : null), PageRequest.of(0, Integer.MAX_VALUE));
        reviews.forEach(r -> {
            if (user.getRole().getName().equals(AppRolesEnum.ROLE_CLIENT.name())) {
                if (r.getClientUsername().equals(user.getUsername())) {
                    try {
                        MyLinkedMap ratingMap = getObjectMapper().readValue(r.getClientReview(), MyLinkedMap.class);
                        Map.Entry ratingEntry = ratingMap.getEntry(0);

                        rating[0] = rating[0] + Double.parseDouble(String.valueOf(ratingEntry.getKey()));
                        review.put(username, rating[0]);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                if (r.getLocalServiceProviderUsername().equals(user.getUsername())) {
                    try {
                        MyLinkedMap ratingMap = getObjectMapper().readValue(r.getLocalServiceProviderReview(), MyLinkedMap.class);
                        Map.Entry ratingEntry = ratingMap.getEntry(0);
                        rating[0] = rating[0] + Double.parseDouble(String.valueOf(ratingEntry.getKey()));
                        review.put(username, rating[0]);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                }
            }

        });


        return review;
    }

    @Override
    public Models.Feedback saveFeedback(FeedbackForm feedbackForm) throws NotFoundException, ParseException {

        Models.AppUser user = userService.getAUser(feedbackForm.getUsername());
        if (user == null) {
            throw new NotFoundException(feedbackForm.getUsername() + " is not found");
        }


        Models.Feedback feedback = new Models.Feedback(user, feedbackForm.getRating(), feedbackForm.getComment());
        notificationService.postNotification(new NotificationModels("Feedback has been received", user.getUsername().concat(", we appreciate your feedback"), "", user.getUsername(), "Feedback"));
        return feedbackRepo.save(feedback);
    }

    @Override
    public Page<Models.Feedback> getAllFeedbacks(FeedbackPredicate feedbackPredicate, PageRequest pageRequest) {
        return feedbackRepo.findAll(feedbackPredicate, pageRequest);
    }

    @Override
    public List<FeedbackChart> getFeedbackChat() {
        List<Models.Feedback> feedbackList = feedbackRepo.findAll();
        List<FeedbackChart> feedbackChartList = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            feedbackChartList.add(new FeedbackChart(i));
        }

        feedbackList.forEach(f -> {
            List<FeedbackChart> feedbacksStream = feedbackChartList.stream().filter(i -> Objects.equals(i.getRating(), f.getRating())).collect(Collectors.toList());
            if (!feedbacksStream.isEmpty()) {
                FeedbackChart feedbackChart = feedbacksStream.get(0);
                feedbackChart.addAmount();
            }
        });

        return feedbackChartList;
    }

    @Override
    public List<JobActivityModel> getOngoingJobs() throws ParseException {

        List<Models.Job> allJobs = jobRepo.findAll();

        Date startToday = DataOps.getNowFormattedFullDate();
        startToday.setMinutes(0);
        startToday.setHours(0);
        startToday.setSeconds(0);
        Date endToday = DataOps.getNowFormattedFullDate();
        endToday.setMinutes(59);
        endToday.setHours(23);
        endToday.setSeconds(59);


        List<Models.Job> ongoingJobs = allJobs.stream().filter(job -> {
            Date jobDate = job.getCreatedAt();
            return jobDate.after(startToday) && jobDate.before(endToday) && ongoingStatus.contains(job.getJobStatus());
        }).collect(Collectors.toList());

        List<JobActivityModel> jobActivityModelList = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            jobActivityModelList.add(new JobActivityModel(i));
        }

        ongoingJobs.forEach(j -> {
            List<JobActivityModel> jobStream = jobActivityModelList.stream().filter(i -> Objects.equals(i.getHour(), j.getCreatedAt().getHours())).collect(Collectors.toList());
            if (!jobStream.isEmpty()) {
                JobActivityModel jm = jobStream.get(0);
                jm.addQuantity();
            }
        });

        return jobActivityModelList;
    }

    @Override
    public List<JobActivityModel> getCompleteJobs() throws ParseException {

        List<Models.Job> allJobs = jobRepo.findAll();

        Date startToday = DataOps.getNowFormattedFullDate();
        startToday.setMinutes(0);
        startToday.setHours(0);
        startToday.setSeconds(0);

        Date endToday = DataOps.getNowFormattedFullDate();
        endToday.setMinutes(59);
        endToday.setHours(23);
        endToday.setSeconds(59);

        List<Integer> completedStatus = Arrays.stream(JobStatus.values()).filter(s -> s.code == JobStatus.COMPLETED.code || s.code == JobStatus.CLIENT_COMPLETE.code || s.code == JobStatus.SERVICE_COMPLETE.code || s.code == JobStatus.RATING.code).map(JobStatus::getCode).collect(Collectors.toList());

        List<Models.Job> completedJobs = allJobs.stream().filter(job -> {
            Date jobDate = job.getCreatedAt();
            return jobDate.after(startToday) && jobDate.before(endToday) && completedStatus.contains(job.getJobStatus());
        }).collect(Collectors.toList());

        List<JobActivityModel> jobActivityModelList = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            jobActivityModelList.add(new JobActivityModel(i));
        }

        completedJobs.forEach(j -> {
            List<JobActivityModel> jobStream = jobActivityModelList.stream().filter(i -> Objects.equals(i.getHour(), j.getCreatedAt().getHours())).collect(Collectors.toList());
            if (!jobStream.isEmpty()) {
                JobActivityModel jm = jobStream.get(0);
                jm.addQuantity();
            }
        });

        return jobActivityModelList;
    }

    @Override
    public List<JobActivityModel> getPendingJobs() throws ParseException {

        List<Models.Job> allJobs = jobRepo.findAll();

        Date startToday = DataOps.getNowFormattedFullDate();
        startToday.setMinutes(0);
        startToday.setHours(0);
        startToday.setSeconds(0);

        Date endToday = DataOps.getNowFormattedFullDate();
        endToday.setMinutes(59);
        endToday.setHours(23);
        endToday.setSeconds(59);


        List<Models.Job> pendingJobs = allJobs.stream().filter(job -> {
            Date jobDate = job.getCreatedAt();
            return jobDate.after(startToday) && jobDate.before(endToday) && pendingStatus.contains(job.getJobStatus());
        }).collect(Collectors.toList());


        List<JobActivityModel> jobActivityModelList = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            jobActivityModelList.add(new JobActivityModel(i));
        }

        pendingJobs.forEach(j -> {
            List<JobActivityModel> jobStream = jobActivityModelList.stream().filter(i -> Objects.equals(i.getHour(), j.getCreatedAt().getHours())).collect(Collectors.toList());
            if (!jobStream.isEmpty()) {
                JobActivityModel jm = jobStream.get(0);
                jm.addQuantity();
            }
        });

        return jobActivityModelList;

    }

    @Override
    public List<JobActivityModel> getFailedJobs() throws ParseException {

        List<Models.Job> allJobs = jobRepo.findAll();

        Date startToday = DataOps.getNowFormattedFullDate();
        startToday.setMinutes(0);
        startToday.setHours(0);
        startToday.setSeconds(0);

        Date endToday = DataOps.getNowFormattedFullDate();
        endToday.setMinutes(59);
        endToday.setHours(23);
        endToday.setSeconds(59);


        List<Models.Job> failedJobs = allJobs.stream().filter(job -> {
            Date jobDate = job.getCreatedAt();
            return jobDate.after(startToday) && jobDate.before(endToday) && failedStatus.contains(job.getJobStatus());
        }).collect(Collectors.toList());

        System.out.println("Failed jobs " + failedJobs.size());

        List<JobActivityModel> jobActivityModelList = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            jobActivityModelList.add(new JobActivityModel(i));
        }

        failedJobs.forEach(j -> {
            List<JobActivityModel> jobStream = jobActivityModelList.stream().filter(i -> Objects.equals(i.getHour(), j.getCreatedAt().getHours())).collect(Collectors.toList());
            if (!jobStream.isEmpty()) {
                JobActivityModel jm = jobStream.get(0);
                jm.addQuantity();
            }
        });

        return jobActivityModelList;

    }

    @Override
    public List<SummaryData> getSummaryData() {
        List<SummaryData> summaryData = new ArrayList<>();
        List<Models.Job> allJobs = jobRepo.findAll();
        List<Models.AppUser> allUsers = userRepo.findAll();

        summaryData.add(new SummaryData(CLIENT_ON_BOARDING, (int) allUsers.stream().filter(i -> i.getRole().getName().equals(AppRolesEnum.ROLE_CLIENT.name())).count()));
        summaryData.add(new SummaryData(LSP_ON_BOARDING, (int) allUsers.stream().filter(i -> i.getRole().getName().equals(AppRolesEnum.ROLE_SERVICE_PROVIDER.name())).count()));




        summaryData.add(new SummaryData(TODAY_PENDING_JOBS, (int) allJobs.stream().filter(i -> pendingStatus.contains(i.getJobStatus())).count()));
        summaryData.add(new SummaryData(TODAY_ONGOING_JOBS, (int) allJobs.stream().filter(i -> ongoingStatus.contains(i.getJobStatus())).count()));
        summaryData.add(new SummaryData(TODAY_FAILED_JOBS, (int) allJobs.stream().filter(i -> failedStatus.contains(i.getJobStatus())).count()));
        summaryData.add(new SummaryData(TODAY_COMPLETED_JOBS, (int) allJobs.stream().filter(i -> completedStatus.contains(i.getJobStatus())).count()));

        return summaryData;
    }
}
