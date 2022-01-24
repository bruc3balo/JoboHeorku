package com.api.jobo.JoboApi.api.controller;


import com.api.jobo.JoboApi.api.domain.BatchMpesaModels;
import com.api.jobo.JoboApi.api.domain.MpesaModels;
import com.api.jobo.JoboApi.utils.ApiCode;
import com.api.jobo.JoboApi.utils.JsonResponse;
import com.api.jobo.JoboApi.utils.JsonSetErrorResponse;
import com.api.jobo.JoboApi.utils.JsonSetSuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.jobo.JoboApi.globals.GlobalService.callBackService;
import static com.api.jobo.JoboApi.utils.DataOps.filterRequestParams;
import static com.api.jobo.JoboApi.utils.DataOps.getObjectMapper;


@RestController
@RequestMapping(value = "/callback")
@Slf4j
public class CallbackController {

    @PostMapping(consumes = "application/json",produces = "application/json")
    public ResponseEntity<?> saveCallBack(@RequestBody BatchMpesaModels.CallbackObject callback) throws IOException {

        System.out.println("callback body :: "+ getObjectMapper().writeValueAsString(callback));

        try {
            BatchMpesaModels.MpesaStkCallback savedCallback = callBackService.saveNewCallback(callback.getBody().getMpesaStkCallback());
            JsonResponse response = JsonSetSuccessResponse.setResponse(HttpStatus.CREATED.value(), "Callback has been successfully saved", null,savedCallback);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage(),null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getCallBacks() {
        try {
            List<BatchMpesaModels.MpesaStkCallback> allCallbacks = callBackService.getAllCallbacks();

            if (allCallbacks.isEmpty()) {
                JsonResponse response = JsonSetSuccessResponse.setResponse(HttpStatus.NOT_FOUND.value(), "No callbacks found", null,allCallbacks);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }



            allCallbacks.forEach(i-> {
                if (i.getCallbackMetadata() != null) {
                    i.getCallbackMetadata().setId(null);
                }
                if (i.getCallbackMetadata().getMpesaCallbackMetadataItems() != null || !i.getCallbackMetadata().getMpesaCallbackMetadataItems().isEmpty()) {
                    i.getCallbackMetadata().getMpesaCallbackMetadataItems().forEach(item-> item.setId(null));
                }
            });

            JsonResponse response = JsonSetSuccessResponse.setResponse(HttpStatus.OK.value(), "Callback retrieved " + allCallbacks.size(), null,allCallbacks);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage(),null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping
    public ResponseEntity<?> deleteCallBacks(HttpServletRequest request, @RequestParam(name = "id") String id) {
        try {

            List<String> unknownParams = filterRequestParams(request, List.of("id"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Boolean deletedCallback = callBackService.deleteCallback(id);

            if (deletedCallback == null) {
                JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete " + id,null);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (!deletedCallback) {
                JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.EXPECTATION_FAILED.value(), "Failed to delete " + id,null);
                return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
            } else {
                JsonResponse response = JsonSetSuccessResponse.setResponse(HttpStatus.OK.value(), "Callback deleted ", null,true);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage(),null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
