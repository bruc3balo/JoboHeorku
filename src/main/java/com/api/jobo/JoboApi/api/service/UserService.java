package com.api.jobo.JoboApi.api.service;




import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.model.NewUserForm;
import com.api.jobo.JoboApi.api.model.RoleCreationForm;
import com.api.jobo.JoboApi.api.model.UserActivityModel;
import com.api.jobo.JoboApi.api.model.UserUpdateForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import javassist.NotFoundException;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.api.jobo.JoboApi.api.domain.Models.*;

public interface UserService {

    //User
    Models.AppUser saveAUser(NewUserForm userForm) throws Exception; //working
    Models.AppUser updateAUser(String username, UserUpdateForm updateForm) throws Exception; //working
    Models.AppUser deleteUser(Models.AppUser user) throws NotFoundException; //works
    Models.AppUser disableUser(Models.AppUser user) throws NotFoundException, ParseException; //works
    Models.AppUser enableUser(Models.AppUser user) throws NotFoundException; //works
    Models.AppUser getAUser(String username); //works
    Page<AppUser> getAllUsers(Specification<AppUser> specification, PageRequest pageRequest); //works
    Page<Models.AppUser> getAllUsers(PageRequest pageRequest); //works
    Page<Models.AppUser> getServiceProviders (String speciality,PageRequest pageRequest);
    List<String> getAllUsernames(); //works
    List<String> getAllNumbers(); //works

    //Role
    // Models.AppRole saveARole(String name) throws NotFoundException; //works
    Models.AppRole saveANewRole(RoleCreationForm form) throws Exception;
    Set<Models.AppRole> saveRolesList(List<RoleCreationForm> creationForms); //works
    Models.AppRole getARole(String name); //works
    List<Models.AppRole> getAllRoles(); //works
    Models.AppUser addARoleToAUser(String username, String roleName) throws Exception; //working


    //Permission
    Models.Permissions saveAPermission(Models.Permissions permissions); //works
    Set<Models.Permissions> savePermissionList (Set<String> permissions);
    Models.Permissions getAPermission(String name); //works
    List<Models.Permissions> getAllPermissions(); //works
    Models.AppRole addAPermissionToARole(String roleName, String permissionName) throws NotFoundException; //works
    Models.AppRole addPermissionListToARole(String roleName, Set<String> permissionName) throws Exception; //works
    List<UserActivityModel> getClientOn_boarding() throws ParseException;
    List<UserActivityModel> getLspOn_boarding() throws ParseException;

    void defaults () throws Exception;



}
