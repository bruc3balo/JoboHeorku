package com.api.jobo.JoboApi.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.api.jobo.JoboApi.globals.GlobalVariables.*;


public class BatchMpesaModels {

    @Getter
    @Setter
    public static class CallbackObject {

        @JsonProperty("Body")
        private MpesaCallBackBody Body;

        public CallbackObject() {
        }
    }


    @Getter
    @Setter
    public static class MpesaCallBackBody {

        @DocumentId
        private String id;

        @JsonProperty("stkCallback")
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

        @DocumentId
        private String id;

        @JsonProperty(value = Item)
        private List<MpesaCallbackMetadataItems> mpesaCallbackMetadataItems = new ArrayList<>();

        public MpesaCallbackMetadata() {

        }

    }

    @Getter
    @Setter
    public static class MpesaCallbackMetadataItems {

        @DocumentId
        private String id;

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

        @DocumentId
        private String id;

        @JsonProperty(value = Body)
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

        @DocumentId
        private String documentId;

        @JsonProperty("id")
        private Long id;

        @JsonProperty(value = MERCHANT_REQUEST_ID)
        private String MerchantRequestID;

        @JsonProperty(value = CHECKOUT_REQUEST_ID)
        private String CheckoutRequestID;

        @JsonProperty(value = RESULT_CODE)
        private Integer ResultCode;

        @JsonProperty(value = RESULT_DESC)
        private String ResultDesc;

        @JsonProperty(value = CALLBACK_METADATA)
        private MpesaCallbackMetadata CallbackMetadata;

        public MpesaStkCallback() {

        }


    }

    @Getter
    @Setter
    public static class STKPushResponseBody {

        @DocumentId
        private String id;

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

}
