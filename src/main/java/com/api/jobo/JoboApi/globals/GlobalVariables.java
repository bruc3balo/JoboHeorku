package com.api.jobo.JoboApi.globals;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GlobalVariables {

    //Unclassified
    public static final String HY = "-";
    public static final String KEY = "^";
    public static final String PACKAGE = " com.api.jobo.JoboApi";
    public static final String SHA1 = "SHA-1";
    public static final String PAGE_NO = "pageNo";
    public static final String PAGE_SIZE = "pageSize";
    public static final String ASAP = "ASAP";


    public static final String PHONE_NUMBER_MPESA = "PhoneNumber";
    public static final String REF_ID = "reference_id";
    public static final String NARRATION = "narration";

    public static final String PAID_RESPONSE = "0";
    public static final String NO_PAID_RESPONSE = "1032";


    //Collections
    public static final String USER_COLLECTION = "users";
    public static final String ROLE_COLLECTION = "roles";
    public static final String PERMISSION_COLLECTION = "permissions";
    public static final String SERVICE_COLLECTION = "services";
    public static final String REVIEW_COLLECTION = "reviews";
    public static final String JOB_COLLECTION = "job";


    //User
    public static final String UID = "uid";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String REPORTED = "reported";
    public static final String ROLE = "role";
    public static final String PREFERRED_WORKING_HOURS = "preferred_working_hours";
    public static final String SPECIALITIES = "specialities";
    public static final String LAST_LOCATION = "last_known_location";
    public static final String BIO = "bio";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String ID_NUMBER = "id_number";
    public static final String NAMES = "names";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String DISABLED = "disabled";
    public static final String DELETED = "deleted";
    public static final String VERIFIED = "verified";
    public static final String ACCESS_TOKEN = "access_token";

    //Role
    public static final String NAME = "name";
    public static final String ID = "id";

    //permissions
    public static final String PERMISSIONS = "permissions";


    //Service
    public static final String DESCRIPTION = "description";

    public static final String PLUMBING = "Plumbing";
    public static final String ELECTRICAL = "Electrical";
    public static final String MECHANICAL = "Mechanic";

    public static final String LAUNDRY = "Laundry";
    public static final String GARDENING = "Gardening";
    public static final String CLEANING = "Cleaning";

    public static final String PAINT_JOB = "Paint Job";
    public static final String MOVING = "Moving";
    public static final String GENERAL_REPAIRS = "General repairs";

    //Review
    public static final String LOCAL_SERVICE_PROVIDER_ID = "local_service_provider_id";
    public static final String LOCAL_SERVICE_PROVIDER_USERNAME = "local_service_provider_username";
    public static final String CLIENT_USERNAME = "client_username";
    public static final String LOCAL_SERVICE_PROVIDER_REVIEW = "local_service_provider_review";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_REVIEW = "client_review";
    public static final String JOB_ID = "job_id";
    public static final String REVIEWED_AT = "reviewed_at";

    //Job
    public static final String JOB_PRICE_RANGE = "job_price_range";
    public static final String JOB_PRICE = "job_price";
    public static final String JOB_LOCATION = "job_location";
    public static final String JOB_DESCRIPTION = "job_description";
    public static final String JOB_STATUS = "job_status";
    public static final String SCHEDULED_AT = "scheduled_at";
    public static final String COMPLETED_AT = "completed_at";

    //payment
    public static final String PAYMENT = "payment";
    public static final String TRANSACTION_DESCRIPTION = "transaction_description";
    public static final String PAY_CATEGORY = "pay_category";
    public static final String PAID = "paid";
    public static final String CONFIRMED_AT = "confirmed_at";

    //mpesa
    public static final String MPESA_TRANSACTION_DESCRIPTION = "transactionDesc";
    public static final String CHECKOUT_REQUEST_ID = "CheckoutRequestID";
    public static final String RESPONSE_BODY = "responseBody";

    //stk
    public static final String BusinessShortCode = "BusinessShortCode";
    public static final String Password = "Password";
    public static final String Timestamp = "Timestamp";
    public static final String TransactionType = "TransactionType";
    public static final String Amount = "Amount";
    public static final String PhoneNumber = "PhoneNumber";
    public static final String PartyA = "PartyA";
    public static final String PartyB = "PartyB";
    public static final String CallBackURL = "CallBackURL";
    public static final String AccountReference = "AccountReference";
    public static final String QueueTimeOutURL = "QueueTimeOutURL";
    public static final String TransactionDesc = "TransactionDesc";
    public static final String transactionType = "CustomerPayBillOnline";

    //reversal
    public static final String SHORT_CODE = "ShortCode";
    public static final String RESPONSE_TYPE = "ResponseType";
    public static final String CONFIRMATION_URL = "ConfirmationURL";
    public static final String VALIDATION_URL = "ValidationURL";

    //balanceInquiry
    public static final String INITIATOR = "Initiator";
    public static final String SECURITY_CREDENTIALS = "SecurityCredential";
    public static final String COMMAND_ID = "CommandID";
    public static final String PARTY_A = "PartyA";
    public static final String IDENTIFIER_TYPE = "IdentifierType";
    public static final String REMARKS = "Remarks";
    public static final String QUEUE_TIME_OUT_URL = "QueueTimeOutURL";
    public static final String RESULT_URL = "ResultURL";


    //b2b
    public static final String SENDER_IDENTIFIER_TYPE = "SenderIdentifierType";
    public static final String RECEIVER_IDENTIFIER_TYPE = "ReceiverIdentifierType";
    public static final String ACCOUNT_REFERENCE = "AccountReference";
    public static final String INITIATOR_NAME = "InitiatorName";
    public static final String OCCASION = "Occasion";

    //c2b
    public static final String MSISDN = "Msisdn";
    public static final String BILL_REF_NUMBER = "BillRefNumber";

    public static final String MERCHANT_REQUEST_ID = "MerchantRequestID";
    public static final String CustomerMessage = "CustomerMessage";
    public static final String ResultCode = "ResultCode";
    public static final String ResultDesc = "ResultDesc";
    public static final String CallbackMetadata = "callbackMetadata";
    public static final String Item = "Item";
    public static final String Name = "Name";
    public static final String Value = "Value";
    public static final String stkCallback = "stkCallback";
    public static final String Body = "Body";
    public static final String Callback = "callback";
    public static final String RESPONSE_CODE = "ResponseCode";
    public static final String RESPONSE_DESCRIPTION = "ResponseDescription";

    //messages
    public static final String THREAD_ID = "threadId";
    public static final String MESSAGE_ID = "messageId";
    public static final String SENDER_UID = "senderUsername";
    public static final String RECEIVER_UID = "receiverUsername";
    public static final String MESSAGE_CONTENT = "messageContent";
    public static final String STATUS = "status";
    public static final String LAST_MODIFIED = "lastModified";
    public static final String MESSAGE_SUR = "MSG";
    public static final String OPENED_AT = "openedAt";
    public static final int DRAFT = 0;
    public static final int QUEUED = 1;
    public static final int SENT = 2;
    public static final int RECEIVED = 3;
    public static final int OPENED = 4;
    public static final String MESSAGES = "Messages";

    public static final String NOTIFICATION = "notification";
    public static final String TITLE = "title";
    public static final String SUBTEXT = "sub_text";
    public static final String NOTIFIED = "notified";
    public static final String UPDATING = "updating";

    public static final int MONTH = 2;
    public static final int YEAR = 1;
    public static final int DAY = 0;

    public static final String CLIENT_ON_BOARDING = "Clients";
    public static final String LSP_ON_BOARDING = "Service providers";
    public static final String TODAY_PENDING_JOBS = "Today's Pending Jobs";
    public static final String TODAY_ONGOING_JOBS = "Today's Ongoing Jobs";
    public static final String TODAY_FAILED_JOBS = "Today's Failed Jobs";
    public static final String TODAY_COMPLETED_JOBS = "Today's Completed Jobs";

    public static final String RESULT_CODE = "ResultCode";
    public static final String RESULT_DESC = "ResultDesc";
    public static final String CALLBACK_METADATA = "CallbackMetadata";

    public static Algorithm myAlgorithm;

    public static Date accessTokenTime;
    public static Date refreshTokenTime;
    public static String contextPath;


    public static String secretJwt;
    public static String tokenPrefix;
    public static Integer tokenExpirationAfterMin;

    public static String jwtAuthHeader;

    public static String appKey;
    public static String appSecret;
    public static String businessShortCode;
    public static String passKey;
    public static String callbackUrl;

    @Autowired
    public void setMyAlgorithm() {
        //todo encrypt the secret
        GlobalVariables.myAlgorithm = Algorithm.HMAC256("secret".getBytes());
    }

    @Autowired
    public void setAccessTokenTime() {        //1day
        GlobalVariables.accessTokenTime = new Date(System.currentTimeMillis() + 3600000);
    }

    @Autowired
    public void setRefreshTokenTIme() {
        GlobalVariables.refreshTokenTime = new Date(System.currentTimeMillis() + 86400000);
        //1day
    }

    @Value("${server.servlet.context-path}")
    public void setContextPath(String contextPath) {
        GlobalVariables.contextPath = contextPath;
    }


    @Value("${application.jwt.secretKey}")
    public void setSecretKey(String secretKey) {
        GlobalVariables.secretJwt = secretKey;
    }

    @Value("${application.jwt.tokenPrefix}")
    public void setTokenPrefix(String tokenPrefix) {
        GlobalVariables.tokenPrefix = tokenPrefix;
    }

    @Value("${application.jwt.tokenExpirationAfterMin}")
    public void setTokenExpirationAfterMin(Integer tokenExpirationAfterMin) {
        GlobalVariables.tokenExpirationAfterMin = tokenExpirationAfterMin;
    }

    @Value("eiaKJaH41Twej5U0KV4ZwHNtc5ABYYDW")
    public void setAppKey(String appKey) {
        GlobalVariables.appKey = appKey;
    }

    @Value("174379")
    public void setBusinessShortCode(String businessShortCode) {
        GlobalVariables.businessShortCode = businessShortCode;
    }

    //@Value("https://mpesa-pi.herokuapp.com/api/v1/pay")
    @Value("https://jobo-spring-api.herokuapp.com/api/v1/callback")
   // @Value("https://posthere.io/d5c6-4134-9fc6")
    public void setCallbackUrl(String callbackUrl) {
        GlobalVariables.callbackUrl = callbackUrl;
    }

    @Value("bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919")
    public void setPassKey(String mPesaPassword) {
        GlobalVariables.passKey = mPesaPassword;
    }

    @Value("6sAGXzDCbByAAdrF")
    public void setAppSecret(String appSecret) {
        GlobalVariables.appSecret = appSecret;
    }

    @Value("Authorization")
    public void setJwtAuthHeader(String jwtAuthHeader) {
        GlobalVariables.jwtAuthHeader = jwtAuthHeader;
    }

}
