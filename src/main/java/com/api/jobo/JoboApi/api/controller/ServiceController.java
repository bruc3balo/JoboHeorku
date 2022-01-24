package com.api.jobo.JoboApi.api.controller;

import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.model.ServiceRequestForm;
import com.api.jobo.JoboApi.api.model.ServiceUpdateForm;
import com.api.jobo.JoboApi.api.specification.ServicePredicate;
import com.api.jobo.JoboApi.utils.ApiCode;
import com.api.jobo.JoboApi.utils.JsonResponse;
import com.api.jobo.JoboApi.utils.JsonSetErrorResponse;
import com.api.jobo.JoboApi.utils.JsonSetSuccessResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.jobo.JoboApi.globals.GlobalService.serviceService;
import static com.api.jobo.JoboApi.utils.DataOps.filterRequestParams;

@RestController
@RequestMapping("/service")
@Slf4j
@Api(value = "Test API Controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"test-api-controller"}, description = "Testing API")
public class ServiceController {


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('service:read')")
    public ResponseEntity<?> getAllServices(HttpServletRequest request,
                                            @RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "id", required = false) Long id) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<Models.Service> serviceList = serviceService.getAllServices(new ServicePredicate(new Models.Service(id, name)));

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, serviceList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('service:write')")
    public ResponseEntity<?> saveAService(HttpServletRequest request, @Valid @RequestBody ServiceRequestForm serviceRequestForm) {

        try {



            Models.Service service = serviceService.saveService(serviceRequestForm);
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, service);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PutMapping("/update")
    @PreAuthorize("hasAuthority('service:update')")
    public ResponseEntity<?> updateAService(HttpServletRequest request,@RequestParam(value = "name") String name, @Valid @RequestBody ServiceUpdateForm serviceUpdateForm) {

        try {

            List<String> unknownParams = filterRequestParams(request, List.of("name"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Models.Service service = serviceService.updateService(name,serviceUpdateForm);
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, service);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
