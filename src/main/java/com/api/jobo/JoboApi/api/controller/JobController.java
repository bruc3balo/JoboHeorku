package com.api.jobo.JoboApi.api.controller;

import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.model.*;
import com.api.jobo.JoboApi.api.specification.FeedbackPredicate;
import com.api.jobo.JoboApi.api.specification.JobPredicate;
import com.api.jobo.JoboApi.api.specification.ReviewPredicate;
import com.api.jobo.JoboApi.utils.ApiCode;
import com.api.jobo.JoboApi.utils.JsonResponse;
import com.api.jobo.JoboApi.utils.JsonSetErrorResponse;
import com.api.jobo.JoboApi.utils.JsonSetSuccessResponse;
import io.swagger.annotations.Api;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static com.api.jobo.JoboApi.globals.GlobalService.jobService;
import static com.api.jobo.JoboApi.globals.GlobalVariables.*;
import static com.api.jobo.JoboApi.utils.DataOps.filterRequestParams;

@RestController
@RequestMapping("/job")
@Slf4j
@Api(value = "Test API Controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"test-api-controller"}, description = "Testing API")
public class JobController {

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('job:read')")
    public ResponseEntity<?> getAllJobs(HttpServletRequest request,
                                        @RequestParam(value = PAGE_SIZE, required = false) Integer pageSize,
                                        @RequestParam(value = PAGE_NO, required = false) Integer pageNo,
                                        @RequestParam(value = CLIENT_USERNAME, required = false) String clientUsername,
                                        @RequestParam(value = JOB_STATUS, required = false) Integer jobStatus,
                                        @RequestParam(value = LOCAL_SERVICE_PROVIDER_USERNAME, required = false) String localServiceProviderUsername,
                                        @RequestParam(value = ID, required = false) Long id) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList(CLIENT_USERNAME, JOB_STATUS, LOCAL_SERVICE_PROVIDER_USERNAME, ID, PAGE_SIZE, PAGE_NO));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            int pageSze = pageSize != null ? pageSize : Integer.MAX_VALUE;
            int pageNumber = pageNo != null ? pageNo : 0;

            Page<Models.Job> jobList = jobService.getAllJobs(new JobPredicate(new Models.Job(id, localServiceProviderUsername, clientUsername, jobStatus)), PageRequest.of(pageNumber, pageSze));

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, jobList.getContent());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('job:write')")
    public ResponseEntity<?> saveAJob(HttpServletRequest request, @RequestBody JobCreationForm form) {

        try {

            Models.Job saveNewJob = jobService.saveNewJob(form);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, saveNewJob);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('job:update')")
    public ResponseEntity<?> updateAJob(HttpServletRequest request, @RequestParam(value = ID) Long jobId, @Valid @RequestBody JobUpdateForm jobUpdateForm) {

        try {

            List<String> unknownParams = filterRequestParams(request, List.of(ID));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Models.Job job = jobService.updateJob(jobId, jobUpdateForm);
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, job);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('job:delete')")
    public ResponseEntity<?> deleteAJob(HttpServletRequest request, @RequestParam(value = ID) Long jobId) {

        try {

            List<String> unknownParams = filterRequestParams(request, List.of(ID));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Boolean deleted = jobService.deleteJob(jobId);
            Map<String, Boolean> res = new HashMap<>();
            res.put("success", deleted);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping("/review/new")
    @PreAuthorize("hasAuthority('review:write')")
    public ResponseEntity<?> saveAReview(HttpServletRequest request, @Valid @RequestBody Models.Review review) {

        try {
            Models.Review saveNewReview = jobService.saveReview(review);
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, saveNewReview);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PutMapping("/review/update")
    @PreAuthorize("hasAuthority('review:update')")
    public ResponseEntity<?> updateAReview(HttpServletRequest request, @Valid @RequestBody Models.Review review) {

        try {
            Models.Review reviewUpdated = jobService.updateReview(review);
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, reviewUpdated);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            if (e instanceof NotFoundException) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/review/all")
    @PreAuthorize("hasAuthority('review:read')")
    public ResponseEntity<?> getAllReviews(HttpServletRequest request,
                                           @RequestParam(value = PAGE_SIZE, required = false) Integer pageSize,
                                           @RequestParam(value = PAGE_NO, required = false) Integer pageNo,
                                           @RequestParam(value = CLIENT_USERNAME, required = false) String clientUsername,
                                           @RequestParam(value = JOB_ID, required = false) Long jobId,
                                           @RequestParam(value = REPORTED, required = false) Boolean reported,
                                           @RequestParam(value = LOCAL_SERVICE_PROVIDER_USERNAME, required = false) String localServiceProviderUsername,
                                           @RequestParam(value = ID, required = false) Long id) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList(CLIENT_USERNAME, REPORTED, JOB_ID, LOCAL_SERVICE_PROVIDER_USERNAME, ID, PAGE_SIZE, PAGE_NO));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            int pageSze = pageSize != null ? pageSize : Integer.MAX_VALUE;
            int pageNumber = pageNo != null ? pageNo : 0;

            Page<Models.Review> reviewList = jobService.getReviews(new ReviewPredicate(new Models.Review(id, localServiceProviderUsername, clientUsername, jobId, reported)), PageRequest.of(pageNumber, pageSze));

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, reviewList.getContent());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/review/{username}")
    public ResponseEntity<?> getAverageUserReview(@PathVariable(name = "username") String username) {
        try {
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, jobService.getUserReview(username));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/feedback")
    public ResponseEntity<?> getFeedback(HttpServletRequest request,
                                         @RequestParam(value = ID, required = false) Long id,
                                         @RequestParam(value = UID, required = false) Long userId,
                                         @RequestParam(value = "rating", required = false) Integer rating,
                                         @RequestParam(value = PAGE_SIZE, required = false) Integer pageSize,
                                         @RequestParam(value = PAGE_NO, required = false) Integer pageNo) {
        try {

            List<String> unknownParams = filterRequestParams(request, Arrays.asList(ID, UID, JOB_ID, "rating", PAGE_SIZE, PAGE_NO));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            int pageSze = pageSize != null ? pageSize : Integer.MAX_VALUE;
            int pageNumber = pageNo != null ? pageNo : 0;

            Page<Models.Feedback> feedbacks = jobService.getAllFeedbacks(new FeedbackPredicate(new Models.Feedback(id, new Models.AppUser(userId), rating)), PageRequest.of(pageNumber, pageSze));
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, feedbacks.getContent());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> saveFeedback(@RequestBody FeedbackForm feedbackForm) {
        try {

            Models.Feedback feedbacks = jobService.saveFeedback(feedbackForm);
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, feedbacks);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/feedback_chart")
    public ResponseEntity<?> getFeedbackChart() {
        try {
            List<FeedbackChart> feedbacks = jobService.getFeedbackChat();
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, feedbacks);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"jobs_ongoing"})
    public ResponseEntity<?> getJobsOnGoing() {

        try {

            List<JobActivityModel> jobList = jobService.getOngoingJobs();

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),ApiCode.SUCCESS.getDescription(), null, jobList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"jobs_pending"})
    public ResponseEntity<?> getJobsPending() {

        try {
            List<JobActivityModel> jobList = jobService.getPendingJobs();
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),ApiCode.SUCCESS.getDescription(), null, jobList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"jobs_complete"})
    public ResponseEntity<?> getJobsCompleted() {

        try {
            List<JobActivityModel> jobList = jobService.getCompleteJobs();
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),ApiCode.SUCCESS.getDescription(), null, jobList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = {"jobs_failed"})
    public ResponseEntity<?> getJobsFailed() {

        try {
            List<JobActivityModel> jobList = jobService.getFailedJobs();
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),ApiCode.SUCCESS.getDescription(), null, jobList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"summary"})
    public ResponseEntity<?> getSummaryData() {

        try {
            List<SummaryData> summaryData = jobService.getSummaryData();
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),ApiCode.SUCCESS.getDescription(), null, summaryData);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
