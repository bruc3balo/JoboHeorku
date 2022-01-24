package com.api.jobo.JoboApi.api.domain;

import com.api.jobo.JoboApi.api.validation.email.ValidEmail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import static com.api.jobo.JoboApi.globals.GlobalVariables.*;
import static com.api.jobo.JoboApi.utils.DataOps.*;


public class Models {

    @Getter
    @Setter
    @Table(name = USER_COLLECTION)
    @Entity
    public static class AppUser {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = NAMES)
        @JsonProperty(value = NAMES)
        private String names;

        @Column(name = USERNAME, unique = true)
        @JsonProperty(USERNAME)
        private String username;

        @Column(name = ID_NUMBER)
        @JsonProperty(ID_NUMBER)
        private String idNumber;

        @Column(name = EMAIL_ADDRESS)
        @JsonProperty(EMAIL_ADDRESS)
        @ValidEmail(message = "Invalid email")
        private String emailAddress;

        @Column(name = PHONE_NUMBER,unique = true)
        @JsonProperty(PHONE_NUMBER)
        private String phoneNumber;

        @Column(name = PASSWORD)
        @JsonProperty(PASSWORD)
        private String password;

        @Column(name = BIO)
        @JsonProperty(BIO)
        private String bio;

        @Column(name = LAST_LOCATION)
        @JsonProperty(LAST_LOCATION)
        private String lastKnownLocation;

        @Column(name = CREATED_AT)
        @JsonProperty(CREATED_AT)
        private Date createdAt;

        @Column(name = UPDATED_AT)
        @JsonProperty(UPDATED_AT)
        private Date updatedAt;

        @JsonProperty(ROLE)
        @ManyToOne(fetch = FetchType.EAGER)
        private AppRole role;

        @Column(name = PREFERRED_WORKING_HOURS)
        @JsonProperty(PREFERRED_WORKING_HOURS)
        private String preferredWorkingHours;

        @JsonProperty(SPECIALITIES)
        @Column(name = SPECIALITIES)
        private String specialities;

        @JsonProperty(DISABLED)
        @Column(name = DISABLED)
        private Boolean disabled;

        @JsonProperty(DELETED)
        @Column(name = DELETED)
        private Boolean deleted;

        @JsonProperty(VERIFIED)
        @Column(name = VERIFIED)
        private Boolean verified;

        @JsonProperty("rating")
        @Column(name = "rating")
        private float rating;

        @JsonProperty("strikes")
        @Column(name = "strikes")
        private int strikes;

        public AppUser() {

        }

        public AppUser(Long id, String names, String username, String idNumber, String emailAddress, String phoneNumber, String password, String bio, String lastKnownLocation, Date createdAt, Date updatedAt, AppRole role, String preferredWorkingHours, String specialities, Boolean disabled, Boolean deleted, Boolean verified) {
            this.id = id;
            this.names = names;
            this.username = username;
            this.idNumber = idNumber;
            this.emailAddress = emailAddress;
            this.phoneNumber = phoneNumber;
            this.password = password;
            this.bio = bio;
            this.lastKnownLocation = lastKnownLocation;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.role = role;
            this.preferredWorkingHours = preferredWorkingHours;
            this.specialities = specialities;
            this.disabled = disabled;
            this.deleted = deleted;
            this.verified = verified;
        }

        public AppUser(String names, String username, String idNumber, String emailAddress, String phoneNumber, String password, String bio, String lastKnownLocation, Date createdAt, Date updatedAt, AppRole role, String preferredWorkingHours, String specialities, Boolean disabled, Boolean deleted, Boolean verified) {
            this.names = names;
            this.username = username;
            this.idNumber = idNumber;
            this.emailAddress = emailAddress;
            this.phoneNumber = phoneNumber;
            this.password = password;
            this.bio = bio;
            this.lastKnownLocation = lastKnownLocation;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.role = role;
            this.preferredWorkingHours = preferredWorkingHours;
            this.specialities = specialities;
            this.disabled = disabled;
            this.deleted = deleted;
            this.verified = verified;

        }


        public AppUser(Long id, String name, String username, String email) {
            this.id = id;
            this.names = name;
            this.username = username;
            this.emailAddress = email;
        }

        public AppUser(Long id) {
            this.id = id;
        }
    }

    @Entity
    @Table(name = ROLE_COLLECTION)
    @Getter
    @Setter
    @AllArgsConstructor
    public static class AppRole {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = NAME)
        @JsonProperty(value = NAME)
        private String name;

        @ManyToMany(fetch = FetchType.EAGER)
        @Column(name = PERMISSIONS)
        private Set<Permissions> permissions = new LinkedHashSet<>();

        public AppRole() {

        }

        public AppRole(String name) {
            this.name = name;
        }

        public AppRole(String name, Set<Permissions> permissions) {
            this.name = name;
            this.permissions = permissions;
        }

        /*public Set<GrantedAuthority> getGrantedAllowedPermissions() {
            return allowedPermissions.stream().map(permissions -> new SimpleGrantedAuthority(permissions.getName())).collect(Collectors.toSet());
        }*/
    }

    @Entity
    @Table(name = PERMISSION_COLLECTION)
    @Getter
    @Setter
    public static class Permissions {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        private String name;

        public Permissions() {
        }

        public Permissions(String name) {
            this.name = name;
        }


    }

    @Getter
    @Setter
    @Entity
    @Table(name = SERVICE_COLLECTION)
    public static class Service {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(value = DESCRIPTION)
        @Column(name = DESCRIPTION)
        private String description;

        @JsonProperty(value = NAME)
        @Column(name = NAME, unique = true)
        private String name;

        @JsonProperty(value = CREATED_AT)
        @Column(name = CREATED_AT)
        private Date createdAt;

        @JsonProperty(value = UPDATED_AT)
        @Column(name = UPDATED_AT)
        private Date updatedAt;

        @JsonProperty(value = DISABLED)
        @Column(name = DISABLED)
        private boolean disabled;

        public Service(Long id, String description, String name, Date createdAt, Date updatedAt, boolean disabled) {
            this.id = id;
            this.description = description;
            this.name = name;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.disabled = disabled;
        }

        public Service(String description, String name, Date createdAt, Date updatedAt, boolean disabled) {
            this.description = description;
            this.name = name;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.disabled = disabled;
        }

        public Service(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Service() {

        }
    }

    @Getter
    @Setter
    @Entity
    @Table(name = REVIEW_COLLECTION)
    public static class Review {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(value = LOCAL_SERVICE_PROVIDER_USERNAME)
        private String localServiceProviderUsername;

        @JsonProperty(value = LOCAL_SERVICE_PROVIDER_REVIEW)
        private String localServiceProviderReview;

        @JsonProperty(value = CLIENT_USERNAME)
        private String clientUsername;

        @JsonProperty(value = CLIENT_REVIEW)
        private String clientReview;

        @JsonProperty(value = JOB_ID)
        @NotNull(message = "missing job id")
        private Long jobId;

        @JsonProperty(value = CREATED_AT)
        private String createdAt;

        @JsonProperty(value = UPDATED_AT)
        private String updatedAt;

        @JsonProperty(value = REPORTED)
        private Boolean reported;


        public Review(Long id, String localServiceProviderUsername, String clientUsername, Long jobId, Boolean reported) {
            this.id = id;
            this.localServiceProviderUsername = localServiceProviderUsername;
            this.clientUsername = clientUsername;
            this.jobId = jobId;
            this.reported = reported;
        }

        public Review() {

        }

        public Review(String localServiceProviderUsername, String clientUsername) {
            this.localServiceProviderUsername = localServiceProviderUsername;
            this.clientUsername = clientUsername;
        }

        public Review(Long jobId) {
            this.jobId = jobId;
        }

        public Review(Long id, String localServiceProviderUsername, String localServiceProviderReview, String clientUsername, String clientReview, Long jobId, String createdAt, String updatedAt, Boolean reported) {
            this.id = id;
            this.localServiceProviderUsername = localServiceProviderUsername;
            this.localServiceProviderReview = localServiceProviderReview;
            this.clientUsername = clientUsername;
            this.clientReview = clientReview;
            this.jobId = jobId;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.reported = reported;
        }
    }

    @Getter
    @Setter
    @Entity
    @Table(name = JOB_COLLECTION)
    public static class Job {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(value = LOCAL_SERVICE_PROVIDER_USERNAME)
        @Column(name = LOCAL_SERVICE_PROVIDER_USERNAME)
        private String localServiceProviderUsername;

        @JsonProperty(value = CLIENT_USERNAME)
        @Column(name = CLIENT_USERNAME)
        private String clientUsername;

        @JsonProperty(value = JOB_LOCATION)
        @Column(name = JOB_LOCATION)
        private String jobLocation;

        @JsonProperty(SPECIALITIES)
        @Column(name = SPECIALITIES)
        private String specialities;

        @JsonProperty(value = CREATED_AT)
        @Column(name = CREATED_AT)
        private Date createdAt;

        @JsonProperty(value = UPDATED_AT)
        @Column(name = UPDATED_AT)
        private Date updatedAt;

        @JsonProperty(value = SCHEDULED_AT)
        @Column(name = SCHEDULED_AT)
        private String scheduledAt;

        @JsonProperty(value = COMPLETED_AT)
        @Column(name = COMPLETED_AT)
        private Date completedAt;

        @JsonProperty(value = JOB_PRICE_RANGE)
        @Column(name = JOB_PRICE_RANGE)
        private String jobPriceRange;

        @JsonProperty(value = JOB_PRICE)
        @Column(name = JOB_PRICE)
        private BigDecimal jobPrice;

        @JsonProperty(value = JOB_DESCRIPTION)
        @Column(name = JOB_DESCRIPTION)
        private String jobDescription;

        @JsonProperty(value = JOB_STATUS)
        @Column(name = JOB_STATUS)
        private Integer jobStatus;

        @JsonProperty(value = REPORTED)
        @Column(name = REPORTED)
        private Boolean reported;

        @ManyToMany(fetch = FetchType.EAGER)
        @Column(name = PAYMENT)
        private Set<Payment> payments = new LinkedHashSet<>();

        public Job() {

        }

        public Job(Long id, String localServiceProviderUsername, String clientUsername, Integer jobStatus) {
            this.id = id;
            this.localServiceProviderUsername = localServiceProviderUsername;
            this.clientUsername = clientUsername;
            this.jobStatus = jobStatus;
        }

        public Job(String localServiceProviderUsername, String clientUsername, String jobLocation, String specialities, Date createdAt, Date updatedAt, String scheduledAt, String jobPriceRange, BigDecimal jobPrice, String jobDescription, Integer jobStatus, Boolean reported) {
            this.localServiceProviderUsername = localServiceProviderUsername;
            this.clientUsername = clientUsername;
            this.jobLocation = jobLocation;
            this.specialities = specialities;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.scheduledAt = scheduledAt;
            this.jobPriceRange = jobPriceRange;
            this.jobPrice = jobPrice;
            this.jobDescription = jobDescription;
            this.jobStatus = jobStatus;
            this.reported = reported;
        }
    }

    @Getter
    @Setter
    @Entity
    @Table(name = PAYMENT)
    public static class Payment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(value = "CheckoutRequestID")
        @Column(name = "checkout_request_id")
        private String checkoutRequestId;

        @JsonProperty(value = "ResponseCode")
        @Column(name = "ResponseCode")
        private String responseCode;

        @JsonProperty(value = PAID)
        @Column(name = PAID)
        private boolean paid;

        @JsonProperty(value = NARRATION)
        @Column(name = NARRATION)
        private String narration;

        @JsonProperty(value = TRANSACTION_DESCRIPTION)
        @Column(name = TRANSACTION_DESCRIPTION)
        private String transactionDescription;

        @JsonProperty(value = PHONE_NUMBER_MPESA)
        @Column(name = PHONE_NUMBER_MPESA)
        private String phoneNumber;

        @JsonProperty(value = JOB_ID)
        @Column(name = JOB_ID)
        private Long jobId;

        @JsonProperty(value = CONFIRMED_AT)
        @Column(name = CONFIRMED_AT)
        private Date confirmedAt;

        @JsonProperty(value = CREATED_AT)
        @Column(name = CREATED_AT, updatable = false)
        private Date createdAt;

        @Column(name = ACCESS_TOKEN)
        @JsonProperty(value = ACCESS_TOKEN)
        private String accessToken;

        @JsonProperty(value = "resultCode")
        @Column(name = ResultCode)
        private Integer resultCode;

        @JsonProperty(value = "Amount")
        @Column(name = "Amount")
        private String Amount;

        @JsonProperty(value = "MpesaReceiptNumber")
        @Column(name = "MpesaReceiptNumber")
        private String mpesaReceiptNumber;

        public Payment() {

        }

        public Payment(String payCategory, String transactionDescription, String phoneNumber, Long jobId) {
            this.narration = payCategory;
            this.transactionDescription = transactionDescription;
            this.phoneNumber = phoneNumber;
            this.jobId = jobId;
        }

        public Payment(String narration, String transactionDescription, String phoneNumber, Long jobId, Date confirmedAt, String accessToken, Date createdAt) {
            this.narration = narration;
            this.transactionDescription = transactionDescription;
            this.phoneNumber = phoneNumber;
            this.jobId = jobId;
            this.confirmedAt = confirmedAt;
            this.accessToken = accessToken;
            this.createdAt = createdAt;
        }

        public Payment(MpesaModels.STKPushResponseBody responseBody, String narration, String transactionDescription, String phoneNumber, Long jobId, Date confirmedAt, String accessToken, Date createdAt) {
            this.checkoutRequestId = responseBody.getCheckoutRequestId();
            this.responseCode = responseBody.getResponseCode();
            this.narration = narration;
            this.transactionDescription = transactionDescription;
            this.phoneNumber = phoneNumber;
            this.jobId = jobId;
            this.confirmedAt = confirmedAt;
            this.accessToken = accessToken;
            this.createdAt = createdAt;
        }

    }

    @Getter
    @Setter
    @Entity
    @Table(name = REPORTED)
    public static class Reported {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @OneToOne(fetch = FetchType.EAGER)
        private Job job;

        @ManyToMany(fetch = FetchType.EAGER)
        @Column(name = MESSAGES)
        private Set<Messages> messages = new HashSet<>();


    }

    @Getter
    @Setter
    @Entity
    @Table(name = MESSAGES)
    public static class Messages implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(THREAD_ID)
        @Column(name = THREAD_ID)
        private String threadId;

        @JsonProperty(MESSAGE_ID)
        @Column(name = MESSAGE_ID)
        private String messageId;

        @JsonProperty(SENDER_UID)
        @Column(name = SENDER_UID)
        private String senderUsername;

        @JsonProperty(RECEIVER_UID)
        @Column(name = RECEIVER_UID)
        private String receiverUsername;

        @JsonProperty(MESSAGE_CONTENT)
        @Column(name = MESSAGE_CONTENT)
        private String messageContent;

        @JsonProperty(CREATED_AT)
        @Column(name = CREATED_AT)
        private String createdAt;

        @JsonProperty(STATUS)
        @Column(name = STATUS)
        private int status;

        @JsonProperty(LAST_MODIFIED)
        @Column(name = LAST_MODIFIED)
        private String lastModified;

        @JsonProperty(OPENED_AT)
        @Column(name = OPENED_AT)
        private String openedAt;


        public Messages() {
        }


        public Messages(String threadId, String messageId, String senderUsername, String receiverUsername, String messageContent, String createdAt, int status, String lastModified, String openedAt) {
            this.threadId = threadId;
            this.messageId = messageId;
            this.senderUsername = senderUsername;
            this.receiverUsername = receiverUsername;
            this.messageContent = messageContent;
            this.createdAt = createdAt;
            this.status = status;
            this.lastModified = lastModified;
            this.openedAt = openedAt;
        }

        public Messages(@NotNull String messageId, String senderUsername, String receiverUsername, String messageContent, String createdAt) {
            this.messageId = messageId;
            this.senderUsername = senderUsername;
            this.receiverUsername = receiverUsername;
            this.messageContent = messageContent;
            this.createdAt = createdAt;
        }

        public @NotNull String getMessageId() {
            return messageId;
        }

        public void setMessageId(@NotNull String messageId) {
            this.messageId = messageId;
        }

        public String getOpenedAt() {
            return openedAt;
        }

        public void setOpenedAt(String openedAt) {
            this.openedAt = openedAt;
        }

        public String getThreadId() {
            return threadId;
        }

        public void setThreadId(String threadId) {
            this.threadId = threadId;
        }

        public String getSenderUsername() {
            return senderUsername;
        }

        public void setSenderUsername(String senderUsername) {
            this.senderUsername = senderUsername;
        }

        public String getReceiverUsername() {
            return receiverUsername;
        }

        public void setReceiverUsername(String receiverUsername) {
            this.receiverUsername = receiverUsername;
        }

        public String getMessageContent() {
            return messageContent;
        }

        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getLastModified() {
            return lastModified;
        }

        public void setLastModified(String lastModified) {
            this.lastModified = lastModified;
        }
    }


    @Getter
    @Setter
    @Entity
    @Table(name = "Feedback")
    public static class Feedback {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @OneToOne(fetch = FetchType.EAGER)
        private Models.AppUser user;

        @Column(name = "rating")
        @JsonProperty(value = "rating")
        private Integer rating;

        @JsonProperty(value = "comment")
        @Column(name = "comment")
        private String comment;

        public Feedback() {
        }

        public Feedback(Long id, AppUser user, Integer rating) {
            this.id = id;
            this.user = user;
            this.rating = rating;
        }

        public Feedback(AppUser user, Integer rating, String comment) {
            this.user = user;
            this.rating = rating;
            this.comment = comment;
        }
    }

    @Getter
    @Setter
    @Entity
    @Table(name = "notification")
    public static class NotificationModels {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(DESCRIPTION)
        @Column(name = DESCRIPTION)
        private String description;

        @JsonProperty(TITLE)
        @Column(name = TITLE)
        private String title;

        @JsonProperty(SUBTEXT)
        @Column(name = SUBTEXT)
        private String subText;

        @JsonProperty(UID)
        @Column(name = UID)
        private String uid;

        @JsonProperty(CREATED_AT)
        @Column(name = CREATED_AT, updatable = false)
        private Date createdAt;

        @JsonProperty(NOTIFIED)
        @Column(name = NOTIFIED)
        private boolean notified;

        @JsonProperty(UPDATING)
        @Column(name = UPDATING)
        private String updating;

        public NotificationModels() {

        }

        public NotificationModels(String description, String title, String subText, String uid,String updating) throws ParseException {
            this.description = description;
            this.title = title;
            this.subText = subText;
            this.uid = uid;
            this.notified = false;
            this.updating = updating;
            this.createdAt = getNowFormattedFullDate();
        }

    }

}
