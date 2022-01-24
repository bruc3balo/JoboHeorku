package com.api.jobo.JoboApi.api.service;

import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.model.*;
import com.api.jobo.JoboApi.api.specification.FeedbackPredicate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.api.jobo.JoboApi.api.domain.Models.Job;

public interface JobService {
    //Create
    Job saveNewJob(JobCreationForm form) throws ParseException, NotFoundException;

    //read
    Optional<Job> getJob(Long jobId);

    Page<Job> getAllJobs(Specification<Job> specification, PageRequest pageRequest);

    Optional<List<Job>> getAllCreatedJobs(String userId);

    Optional<List<Job>> getAllRequestedJobs(String userId);

    //update
    Job updateJob(Long jobId, JobUpdateForm form) throws Exception;

    //delete
    boolean deleteJob(Long jobId) throws NotFoundException;

    //review
    Models.Review saveReview(Models.Review review) throws DuplicateRequestException, NotFoundException, ParseException, JsonProcessingException;
    Models.Review updateReview(Models.Review review) throws NotFoundException, ParseException, JsonProcessingException;
    Page<Models.Review> getReviews(Specification<Models.Review> specification, PageRequest pageRequest);
    HashMap<String,Double> getUserReview(String username) throws NotFoundException;

    Models.Feedback saveFeedback(FeedbackForm feedbackForm) throws NotFoundException, ParseException;
    Page<Models.Feedback> getAllFeedbacks(FeedbackPredicate feedbackPredicate,PageRequest pageRequest);
    List<FeedbackChart> getFeedbackChat ();

    List<JobActivityModel> getOngoingJobs() throws ParseException;
    List<JobActivityModel> getPendingJobs() throws ParseException;
    List<JobActivityModel> getCompleteJobs() throws ParseException;
    List<JobActivityModel> getFailedJobs() throws ParseException;
    List<SummaryData> getSummaryData() throws ParseException;

}
