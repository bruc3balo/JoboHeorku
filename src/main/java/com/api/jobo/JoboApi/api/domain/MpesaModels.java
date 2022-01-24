package com.api.jobo.JoboApi.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.api.jobo.JoboApi.globals.GlobalVariables.*;


public class MpesaModels {

    @Getter
    @Setter
    public static class MpesaAuthResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private String expiresIn;

        public MpesaAuthResponse(String accessToken, String expiresIn) {
            this.accessToken = accessToken;
            this.expiresIn = expiresIn;
        }

        public MpesaAuthResponse() {

        }
    }

    @Getter
    @Setter
    public static class MpesaCallBackBody {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(value = stkCallback)
        private MpesaStkCallback mpesaStkCallback;

        public MpesaCallBackBody() {

        }

        public MpesaCallBackBody(MpesaStkCallback mpesaStkCallback) {
            this.mpesaStkCallback = mpesaStkCallback;
        }
    }

    @Getter
    @Setter

    public static class MpesaCallbackMetadata {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(value = Item)
        private List<MpesaCallbackMetadataItems> mpesaCallbackMetadataItems = new ArrayList<>();

        public MpesaCallbackMetadata() {

        }

    }

    @Getter
    @Setter
    public static class MpesaCallbackMetadataItems {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(value = Name)
        private String name;

        @JsonProperty(value = Value)
        private String value;

        public MpesaCallbackMetadataItems() {

        }

        public MpesaCallbackMetadataItems(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    @Getter
    @Setter
    public static class MpesaCallBackModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(value = Body)
        @ManyToOne(fetch = FetchType.EAGER)
        private MpesaCallBackBody body;

        public MpesaCallBackModel() {
        }

        public MpesaCallBackModel(MpesaCallBackBody body) {
            this.body = body;
        }
    }

    @Getter
    @Setter
    public static class MpesaStkCallback {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(value = "merchantRequestID")
        @Column(name = MERCHANT_REQUEST_ID)
        private String merchantRequestID;

        @JsonProperty(value = "checkoutRequestID")
        @Column(name = CHECKOUT_REQUEST_ID)
        private String checkoutRequestID;

        @JsonProperty(value = "resultCode")
        @Column(name = ResultCode)
        private int resultCode;

        @JsonProperty(value = "resultDesc")
        @Column(name = ResultDesc)
        private String resultDesc;

        @JsonProperty(value = "callbackMetadata")
        @ManyToOne(fetch = FetchType.EAGER)
        private MpesaCallbackMetadata callbackMetadata;

        public MpesaStkCallback() {

        }
    }

    @Getter
    @Setter
    public static class STKPushResponseBody {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(value = "MerchantRequestID")
        private String merchantRequestId;

        @JsonProperty(value = "ResponseCode")
        private String responseCode;

        @JsonProperty(value = "CustomerMessage")
        private String customerMessage;

        @JsonProperty(value = "CheckoutRequestID")
        private String checkoutRequestId;

        @JsonProperty(value = "ResponseDescription")
        private String responseDescription;

        public STKPushResponseBody() {
        }

        public STKPushResponseBody(String merchantRequestId, String responseCode, String customerMessage, String checkoutRequestId, String responseDescription) {
            this.merchantRequestId = merchantRequestId;
            this.responseCode = responseCode;
            this.customerMessage = customerMessage;
            this.checkoutRequestId = checkoutRequestId;
            this.responseDescription = responseDescription;
        }
    }

    @Getter
    @Setter

    public static class QueryResponse {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty(value = "ResponseCode")
        private int ResponseCode;

        @JsonProperty(value = "ResponseDescription")
        private String ResponseDescription;

        @JsonProperty(value = "MerchantRequestID")
        private String MerchantRequestID;

        @JsonProperty(value = "CheckoutRequestID")
        private String CheckoutRequestID;

        @JsonProperty(value = "ResultCode")
        private int ResultCode;

        @JsonProperty(value = "ResultDesc")
        private String ResultDesc;

        public QueryResponse() {
        }

        public QueryResponse(int responseCode, String responseDescription, String merchantRequestID, String checkoutRequestID, int resultCode, String resultDesc) {
            ResponseCode = responseCode;
            ResponseDescription = responseDescription;
            MerchantRequestID = merchantRequestID;
            CheckoutRequestID = checkoutRequestID;
            ResultCode = resultCode;
            ResultDesc = resultDesc;
        }
    }




}
