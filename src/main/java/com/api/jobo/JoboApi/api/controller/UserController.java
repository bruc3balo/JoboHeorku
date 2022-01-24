package com.api.jobo.JoboApi.api.controller;


import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.domain.Models.AppUser;
import com.api.jobo.JoboApi.api.model.*;
import com.api.jobo.JoboApi.api.specification.UserPredicate;
import com.api.jobo.JoboApi.config.jwt.TokenHelper;
import com.api.jobo.JoboApi.config.security.AppRolesEnum;
import com.api.jobo.JoboApi.utils.ApiCode;
import com.api.jobo.JoboApi.utils.JsonResponse;
import com.api.jobo.JoboApi.utils.JsonSetErrorResponse;
import com.api.jobo.JoboApi.utils.JsonSetSuccessResponse;
import com.sun.jdi.request.DuplicateRequestException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.jobo.JoboApi.globals.GlobalService.jobService;
import static com.api.jobo.JoboApi.globals.GlobalService.userService;
import static com.api.jobo.JoboApi.globals.GlobalVariables.*;
import static com.api.jobo.JoboApi.utils.DataOps.filterRequestParams;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(value = "Test API Controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"test-api-controller"}, description = "Testing API")
public class UserController {

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request,
                                         @RequestParam(value = PAGE_SIZE, required = false) Integer pageSize,
                                         @RequestParam(value = PAGE_NO, required = false) Integer pageNo,
                                         @RequestParam(value = EMAIL_ADDRESS, required = false) String email,
                                         @RequestParam(value = NAME, required = false) String name,
                                         @RequestParam(value = USERNAME, required = false) String username,
                                         @RequestParam(value = ID, required = false) Long id) {


        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList(NAME, ID,USERNAME, EMAIL_ADDRESS,PAGE_NO,PAGE_SIZE));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            int pageSze = pageSize != null ? pageSize : Integer.MAX_VALUE;
            int pageNumber = pageNo != null ? pageNo : 0;


            Page<AppUser> userList = userService.getAllUsers(new UserPredicate(new AppUser(id, name, username, email)), PageRequest.of(pageNumber, pageSze));
            
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, userList.getContent());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping(value = {"admin"})
    public ResponseEntity<?> getAllWebAdmin(HttpServletRequest request, @RequestParam(value = USERNAME) String username) {


        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList(USERNAME));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }



            AppUser user = userService.getAUser(username);
            if (!user.getRole().getName().equals(AppRolesEnum.ROLE_ADMIN.name())) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping
    public ResponseEntity<?> getAllWebUsers(HttpServletRequest request,
                                            @RequestParam(value = PAGE_SIZE, required = false) Integer pageSize,
                                            @RequestParam(value = PAGE_NO, required = false) Integer pageNo,
                                            @RequestParam(value = EMAIL_ADDRESS, required = false) String email,
                                            @RequestParam(value = NAME, required = false) String name,
                                            @RequestParam(value = USERNAME, required = false) String username,
                                            @RequestParam(value = ID, required = false) Long id) {


        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList(NAME, ID,USERNAME, EMAIL_ADDRESS,PAGE_NO,PAGE_SIZE));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            int pageSze = pageSize != null ? pageSize : Integer.MAX_VALUE;
            int pageNumber = pageNo != null ? pageNo : 0;


            Page<AppUser> userList = userService.getAllUsers(new UserPredicate(new AppUser(id, name, username, email)), PageRequest.of(pageNumber, pageSze));

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, userList.getContent());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("/numbers")
    //@PreAuthorize("hasIp('')")
    public ResponseEntity<?> getAllPhoneNumbers() {
        try {
            List<String> numbersList = userService.getAllNumbers();
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, numbersList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usernames")
    //@PreAuthorize("hasIp('')")
    public ResponseEntity<?> getAllUsernames() {

        try {
            List<String> usernamesList = userService.getAllUsernames();
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, usernamesList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/providers")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<?> getSpecialityWorkers(HttpServletRequest request,
                                                  @RequestParam(value = PAGE_SIZE, required = false) String pageSize,
                                                  @RequestParam(value = PAGE_NO, required = false) String pageNo,
                                                  @RequestParam(name = SPECIALITIES) String speciality) {

        try {

            log.info("PARAMETERS ARE pageNo"+pageNo + " pageSize" + pageSize + " speciality" + speciality);

            List<String> unknownParams = filterRequestParams(request, Arrays.asList(SPECIALITIES,PAGE_NO,PAGE_SIZE));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            int pageSze = pageSize != null ? Integer.parseInt(pageSize) : Integer.MAX_VALUE;
            int pageNumber = pageNo != null ? Integer.parseInt(pageNo) : 0;

            Page<Models.AppUser> providerList = userService.getServiceProviders(speciality,PageRequest.of(pageNumber,pageSze));
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, providerList.getContent());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping("/save")
    //@PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> saveUser(@Valid @RequestBody NewUserForm newUserForm) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
        log.info("uri saveuser ::: {}", uri);

        try {
            AppUser savedUser = userService.saveAUser(newUserForm);
            JsonResponse response = JsonSetSuccessResponse.setResponse(savedUser != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), savedUser != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, savedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {


            if (e instanceof DuplicateRequestException) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/update")
    //@PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<?> updateUser(HttpServletRequest request,
                                        @RequestParam(value = "username") String username,
                                        @RequestBody UserUpdateForm updateForm) {


        try {

            List<String> unknownParams = filterRequestParams(request, List.of("username"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            log.info("UPDATED RECEIVED "+new ObjectMapper().writeValueAsString(updateForm) + " for user "+username);


            AppUser updatedUser = userService.updateAUser(username, updateForm);
            JsonResponse response = JsonSetSuccessResponse.setResponse(updatedUser != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), updatedUser != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, updatedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/saveRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> saveRole(@RequestBody @Valid RoleCreationForm form) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/saveRole").toUriString());
        log.info("uri saverole ::: {}", uri);

        try {
            Models.AppRole savedRole = userService.saveANewRole(form);
            JsonResponse response = JsonSetSuccessResponse.setResponse(savedRole != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), savedRole != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, savedRole);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/role2user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> addRoleToUser(@Valid @RequestBody RoleToUserForm form) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/addroletouser").toUriString());


        try {
            userService.addARoleToAUser(form.getUsername(), form.getRoleName());
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @DeleteMapping(value = "delete")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<?> deleteUser(HttpServletRequest request,
                                        @RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "username", required = false) String username,
                                        @RequestParam(value = "id", required = false) Long id) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id", "username"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<AppUser> userToBeDeleted = userService.getAllUsers(new UserPredicate(id, name, username), PageRequest.of(0, 1)).getContent();

            if (userToBeDeleted.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            AppUser updatedUser = userService.deleteUser(userToBeDeleted.get(0));


            JsonResponse response = JsonSetSuccessResponse.setResponse(updatedUser != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), updatedUser != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, updatedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "disable")
    //@PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<?> disableUser(HttpServletRequest request,
                                         @RequestParam(value = "disabled") Boolean disabled,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "username", required = false) String username,
                                         @RequestParam(value = "id", required = false) Long id) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id", "username", "disabled"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<AppUser> userToBeDeleted = userService.getAllUsers(new UserPredicate(id, name, username), PageRequest.of(0, 1)).getContent();

            if (userToBeDeleted.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if (userToBeDeleted.get(0).getDisabled() == disabled) {
                JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), "User already has that status", null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            AppUser updatedUser;
            if (disabled) {
                updatedUser = userService.disableUser(userToBeDeleted.get(0));
            } else {
                updatedUser = userService.enableUser(userToBeDeleted.get(0));
            }

            JsonResponse response = JsonSetSuccessResponse.setResponse(updatedUser != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), updatedUser != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, updatedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, @RequestParam(name = ACCESS_TOKEN) String token) {

        try {
            List<String> unknownParams = filterRequestParams(request, List.of(ACCESS_TOKEN));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            String refresh = TokenHelper.refreshToken(token);

            if (refresh == null) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Invalid token", null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Map<String, String> map = new HashMap<>();
            map.put("refresh_token",refresh);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),ApiCode.SUCCESS.getDescription(), null, map);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"clients_on_boarding"})
    public ResponseEntity<?> getClientsOnBoarding() {

        try {
            List<UserActivityModel> userList = userService.getClientOn_boarding();
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),ApiCode.SUCCESS.getDescription(), null, userList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"lsp_on_boarding"})
    public ResponseEntity<?> getLspOnBoarding() {

        try {
            List<UserActivityModel> userList = userService.getLspOn_boarding();
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),ApiCode.SUCCESS.getDescription(), null, userList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

