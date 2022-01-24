package com.api.jobo.JoboApi.api.service;


import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.domain.Models.NotificationModels;
import com.api.jobo.JoboApi.api.model.*;
import com.api.jobo.JoboApi.api.specification.UserPredicate;
import com.api.jobo.JoboApi.config.security.AppRolesEnum;
import com.api.jobo.JoboApi.config.security.AppUserPermission;
import com.api.jobo.JoboApi.utils.ConvertDate;
import com.api.jobo.JoboApi.utils.ConvertToJson;
import com.api.jobo.JoboApi.utils.DataOps;
import com.api.jobo.JoboApi.utils.JobStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.api.jobo.JoboApi.globals.GlobalRepo.*;
import static com.api.jobo.JoboApi.globals.GlobalService.*;
import static com.api.jobo.JoboApi.globals.GlobalVariables.*;
import static com.api.jobo.JoboApi.utils.DataOps.*;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {


    //User
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Models.AppUser appUser = userRepo.findByUsername(username).orElse(null);

        if (appUser == null) {
            log.error("User not found in db");
            throw new UsernameNotFoundException("User not found in db");
        } else {
            log.info("User {} found in db ", appUser.getUsername());
        }

        //add all authorities and permissions to list
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(appUser.getRole().getName()));
        appUser.getRole().getPermissions().forEach(p -> {
            if (!authorities.contains(new SimpleGrantedAuthority(p.getName()))) {
                log.info("Adding permissions {} to role {}", appUser.getRole().getName(), p);
                authorities.add(new SimpleGrantedAuthority(p.getName()));
            } else {
                System.out.println("Role already has permission");
            }
        });


        log.info("authorities " + authorities);

        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }

    @Override
    public Models.AppUser saveAUser(NewUserForm newUserForm) throws Exception {
        log.info(newUserForm.getUsername() + " IS BEING CREATED ===================");
        Page<Models.AppUser> users = getAllUsers(new UserPredicate(newUserForm.getUsername(), null), PageRequest.of(0, 1));

        if (!users.isEmpty()) {
            if (users.getContent().get(0).getUsername().equals(newUserForm.getUsername())) {
                throw new DuplicateRequestException("User has already been created");
            }
        }

        // mapper.convertValue(newUserForm.getPreferredWorkingHours(), HashMap.class);

        Models.AppUser newUser = new Models.AppUser(newUserForm.getNames(), newUserForm.getUsername(), newUserForm.getIdNumber(), newUserForm.getEmailAddress(), newUserForm.getPhoneNumber(), passwordEncoder.encode(newUserForm.getPassword()), newUserForm.getBio(), HY, getNowFormattedFullDate(), getNowFormattedFullDate(), null, getStringFromMap(newUserForm.getPreferredWorkingHours()), getStringFromList(newUserForm.getSpecialities()), false, false, false);

        log.info("Saving new user {} to db", newUser.getUsername());

        Models.AppUser createdUser = userRepo.save(newUser);

        if (newUserForm.getRole() != null) {

            if (!newUserForm.getRole().isBlank()) {
                if (!newUserForm.getRole().isEmpty()) {
                    try {
                        if (createdUser.getUsername() != null) {
                            log.info("Now add role {} to user {}", newUserForm.getRole(), createdUser.getUsername());
                            Thread.sleep(1000);
                            return addARoleToAUser(createdUser.getUsername(), newUserForm.getRole());
                        }
                    } catch (NotFoundException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else {
            log.info("No role for user " + createdUser.getUsername());
            try {
                if (createdUser.getUsername() != null) {
                    log.info("Now add role {} to user {}", newUserForm.getRole(), AppRolesEnum.ROLE_CLIENT.name());
                    Thread.sleep(1000);
                    return addARoleToAUser(createdUser.getUsername(), newUserForm.getRole());
                }
            } catch (NotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        return newUser;
    }

    @Override
    public Models.AppUser updateAUser(String username, UserUpdateForm updateForm) throws Exception {
        Models.AppUser user = getAUser(username);

        if (user == null) {
            throw new UsernameNotFoundException(username + ", not found");
        }

        if (updateForm != null) {

            if (updateForm.getRole() != null) {
                addARoleToAUser(user.getUsername(), updateForm.getRole());
            }

            if (updateForm.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(updateForm.getPassword()));
            }

            if (updateForm.getEmailAddress() != null) {
                user.setEmailAddress(updateForm.getEmailAddress());
            }

            if (updateForm.getNames() != null) {
                user.setNames(updateForm.getNames());
            }

            if (updateForm.getIdNumber() != null) {
                user.setIdNumber(updateForm.getIdNumber());
            }

            if (updateForm.getBio() != null) {
                user.setBio(updateForm.getBio());
            }

            if (updateForm.getPreferredWorkingHours() != null) {
                user.setPreferredWorkingHours(getStringFromMap(updateForm.getPreferredWorkingHours()));
            }

            if (updateForm.getSpecialities() != null) {
                user.setSpecialities(getStringFromList(updateForm.getSpecialities()));
            }

            if (updateForm.getVerified() != null) {
                user.setVerified(updateForm.getVerified());
                notificationService.postNotification(new NotificationModels("Verification is successful", user.getUsername().concat(", you have been verified"), "Welcome", user.getUsername(), USER_COLLECTION));
            }

            if (updateForm.getDeleted() != null) {
                user.setDeleted(updateForm.getDeleted());
            }

            if (updateForm.getDisabled() != null) {
                user.setDisabled(updateForm.getDisabled());
            }

            if (updateForm.getPhoneNumber() != null) {
                user.setPhoneNumber(updateForm.getPhoneNumber());
            }

            if (updateForm.getRating() != null) {
                user.setRating(updateForm.getRating());
            }

            if (updateForm.getStrikes() != null) {
                user.setStrikes(updateForm.getStrikes());
            }

            if (updateForm.getLocation() != null) {
                user.setLastKnownLocation(updateForm.getLocation());
            }

            user.setUpdatedAt(getNowFormattedFullDate());

        }

        return userRepo.save(user);
    }

    @Override
    public Models.AppUser getAUser(String username) {
        log.info("Fetching user {} ", username);
        Optional<Models.AppUser> user = userRepo.findByUsername(username);
        return user.orElse(null);
    }

    @Override
    public Models.AppUser deleteUser(Models.AppUser user) {
        user.setDeleted(true);
        return userRepo.save(user);
    }

    @Override
    public Models.AppUser disableUser(Models.AppUser user) throws ParseException {
        user.setDisabled(true);
        notificationService.postNotification(new NotificationModels("Your account has been disabled", user.getUsername().concat(", you have been disabled"), "", user.getUsername(), USER_COLLECTION));
        return userRepo.save(user);
    }

    @Override
    public Models.AppUser enableUser(Models.AppUser user) {
        user.setDisabled(false);
        return userRepo.save(user);
    }

    @Override
    public Page<Models.AppUser> getAllUsers(Specification<Models.AppUser> specification, PageRequest pageRequest) {
        log.info("Fetching all users");

        Page<Models.AppUser> usersAll = userRepo.findAll(specification, pageRequest);
        usersAll.forEach(u -> u.getRole().setPermissions(u.getRole().getPermissions().stream().sorted(Comparator.comparing(Models.Permissions::getId)).collect(Collectors.toCollection(LinkedHashSet::new))));

        // usersAll.forEach(u -> u.setPreferredWorkingHours(u.getPreferredWorkingHours().replaceAll("[\\[\\]]", "")));
        // usersAll.forEach(u -> u.setSpecialities(u.getSpecialities().replaceAll("[\\[\\]]", "")));

        return usersAll;
    }

    @Override
    public Page<Models.AppUser> getAllUsers(PageRequest pageRequest) {
        return userRepo.findAll(pageRequest);
    }

    @Override
    public List<String> getAllNumbers() {
        return userRepo.findAll().stream().filter(f -> f.getPhoneNumber() != null && !f.getPhoneNumber().isEmpty() && !f.getPhoneNumber().isBlank() && !f.getPhoneNumber().equals(HY)).map(Models.AppUser::getPhoneNumber).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllUsernames() {
        return userRepo.findAll().stream().filter(f -> f.getUsername() != null && !f.getUsername().isEmpty() && !f.getUsername().isBlank() && !f.getUsername().equals(HY)).map(Models.AppUser::getUsername).collect(Collectors.toList());
    }

    @Override
    public Page<Models.AppUser> getServiceProviders(String speciality, PageRequest pageRequest) {
        return new PageImpl<>(userRepo.findAll(pageRequest).stream().filter(u -> getListFromString(u.getSpecialities()).contains(speciality)).collect(Collectors.toList()));
    }

    //Role
    /*@Override
    public Models.AppRole saveARole(String name) {
        if (getARole(name) != null) {
            throw new DuplicateRequestException("Role has already been created");
        } else {
            log.info("Saving new role {} to db", name);
            return roleRepo.save(new Models.AppRole(name));
        }
    }*/

    @Override
    public Models.AppRole saveANewRole(RoleCreationForm form) throws Exception {
        if (getARole(form.getName()) != null) {
            throw new DuplicateRequestException("Role has already been created");
        } else {
            log.info("Saving new role {} to db with permissions {}", form.getName(), form.getPermissions().size());

            Models.AppRole role = appRoleRepo.save(new Models.AppRole(form.getName()));

            return addPermissionListToARole(role.getName(), form.getPermissions());
        }
    }

    @Override
    public Set<Models.AppRole> saveRolesList(List<RoleCreationForm> creationForms) {

        Set<Models.AppRole> savedRoles = new HashSet<>();

        creationForms.forEach(f -> {
            try {
                Thread.sleep(1000);
                Models.AppRole role = saveANewRole(f);
                if (role != null) {
                    savedRoles.add(role);
                    log.info("ROle {} saved with permissions {}", role.getName(), role.getPermissions().size());
                    //todo fix
                }
            } catch (DuplicateRequestException e) {
                log.error("Role already added");
            } catch (NotFoundException e) {
                e.printStackTrace();
                log.error("Failed to add role " + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return savedRoles;
    }

    @Override
    public Models.AppRole getARole(String name) {
        log.info("Fetching role {} ", name);
        Optional<Models.AppRole> role = appRoleRepo.findByName(name);
        return role.orElse(null);
    }

    @Override
    public List<Models.AppRole> getAllRoles() {
        log.info("Fetching all roles");
        return appRoleRepo.findAll();
    }

    @Override
    public Models.AppUser addARoleToAUser(String username, String roleName) throws Exception {
        Models.AppUser user = getAUser(username);
        Models.AppRole role = getARole(roleName);

        if (user == null) {
            throw new UsernameNotFoundException("User not found " + username);
        }

        if (role == null) {
            throw new NotFoundException("Role not found " + roleName);
        }

        if (user.getRole() != null) {

            if (user.getRole().getName().equals(role.getName())) {
                throw new DuplicateRequestException("User already has role " + role);
            }


            if (user.getRole().getPermissions().isEmpty()) { //check if permissions is empty
                try {
                    Set<String> permissionsList = Enum.valueOf(AppRolesEnum.class, role.getName()).getGrantedAuthorities().stream().filter(i -> !Objects.equals(i, DataOps.getGrantedAuthorityRole(role.getName()))).map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());
                    addPermissionListToARole(role.getName(), permissionsList);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                //do nothing
                log.info("No changes made for " + username);
            }

        } else { //role doesn't exists
            log.info("Adding role {} to seller {}", role.getName(), user.getUsername()); //will save because @Transactional
        }

        user.setUpdatedAt(getNowFormattedFullDate());
        user.setRole(role);

        return user;
    }

    @Override
    public Models.Permissions saveAPermission(Models.Permissions permissions) {
        if (getAllPermissions().stream().map(Models.Permissions::getName).collect(Collectors.toList()).contains(permissions.getName())) {
            throw new DuplicateRequestException("Permission already in db");
        } else {
            return permissionsRepo.save(permissions);
        }
    }

    @Override
    public Set<Models.Permissions> savePermissionList(Set<String> permissions) {
        Set<Models.Permissions> permissionsSet = new HashSet<>();
        permissions.forEach(p -> {
            Models.Permissions permission = new Models.Permissions(p);
            saveAPermission(permission);
            permissionsSet.add(permission);
        });
        return permissionsSet;
    }

    @Override
    public Models.AppRole addPermissionListToARole(String roleName, Set<String> permissionList) throws Exception {
        log.info("{} Permissions to add {}", roleName, permissionList.size());


        Models.AppRole role = getARole(roleName);
        if (role == null) {
            throw new Exception("Role not found " + roleName);
        }

        List<Models.Permissions> allPermissions = getAllPermissions();

        //save permissions in db
        Set<Models.Permissions> permissionsExistingInDb = new HashSet<>(); //filter out present roles// match names
        Set<Models.Permissions> newAllowedPermissions = new HashSet<>();
        Set<Models.Permissions> rolePermissions = new HashSet<>();

        permissionList.forEach(p -> {
            if (allPermissions.stream().map(Models.Permissions::getName).collect(Collectors.toList()).contains(p)) {
                allPermissions.forEach(dbPermission -> {
                    if (dbPermission.getName().equals(p)) {
                        permissionsExistingInDb.add(dbPermission);
                        rolePermissions.add(dbPermission);
                        log.info("Adding {} to list ", dbPermission.getName());
                    }
                });
            } else {
                newAllowedPermissions.add(new Models.Permissions(p));
                log.info("Adding new {} to list ", p);

            }
        });

        log.info("{} Permissions in db {}", roleName, permissionsExistingInDb.size());

        //save new permissions to db
        final int[] i = {0};
        try {
            newAllowedPermissions.forEach(p -> {
                try {
                    Models.Permissions newPermission = saveAPermission(p);
                    i[0]++;
                    if (newPermission != null) {
                        rolePermissions.add(newPermission);
                        log.info("Adding permission {} to role {}", newPermission.getName(), roleName);
                    }
                } catch (DuplicateRequestException e) {
                    log.error("Permission already in db");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        role.getPermissions().addAll(rolePermissions);
        log.info("{} Permissions not in db {}", roleName, i[0]++);

        return role;
    }

    @Override
    public Models.AppRole addAPermissionToARole(String roleName, String permissionName) throws NotFoundException {
        Models.AppRole role = getARole(roleName);
        Models.Permissions permissions = getAPermission(permissionName);

        if (role == null) {
            throw new NotFoundException("Role not found " + roleName);
        }

        if (permissions == null) {
            throw new NotFoundException("Permission not found " + permissionName + " for role " + roleName);
        }

        if (role.getPermissions().contains(permissions)) {
            throw new DuplicateRequestException("Role " + roleName + " already has permission " + permissionName);
        }


        log.info("Adding permission {} to role {}", permissions.getName(), role.getName());
        role.getPermissions().add(permissions); //will save because @Transactional

        return role;
    }

    @Override
    public List<Models.Permissions> getAllPermissions() {
        return permissionsRepo.findAll();
    }

    @Override
    public Models.Permissions getAPermission(String name) {
        return permissionsRepo.findByName(name).orElse(null);
    }

    @Override
    public List<UserActivityModel> getClientOn_boarding() {

        List<UserActivityModel> activityList = new ArrayList<>();
        List<Models.AppUser> allClients = getAllUsers(new UserPredicate(null, AppRolesEnum.ROLE_CLIENT.name()), PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        //allClients.sort(Comparator.comparing(Models.AppUser::getCreatedAt));



        allClients.forEach(c -> {
            Date date = c.getCreatedAt();
            count++;
            date.setMinutes(0);
            date.setHours(0);
            date.setSeconds(0);

            if (activityList.stream().filter(a -> {

                System.out.println(a.getDate().getYear() +" is equal to "+ date.getYear() + " and "+ a.getDate().getMonth() +" is equal to"+ date.getMonth());

                return a.getDate().getYear() == date.getYear() && a.getDate().getMonth() == date.getMonth();
            }).findFirst().isEmpty()) {
                activityList.add(new UserActivityModel(date));
                System.out.println("adding date " + date + "for "+c.getUsername());
            } else {
                System.out.println("already added " + date + "for "+c.getUsername());
            }

        });

        allClients.forEach(c -> {

            Date date = c.getCreatedAt();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            System.out.println("time cal : "+cal.getTime() + " c time : "+date);

            cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),0,0,0,0);

            List<UserActivityModel> userStream = activityList.stream().filter(a -> {
                Calendar calA = Calendar.getInstance();
                calA.setTime(a.getDate());
                calA.set(calA.get(Calendar.YEAR),calA.get(Calendar.MONTH),0,0,0,0);

                System.out.println(c.getUsername() + " belongs in "+ a.getDate() + " with "+date);

                return calA.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && calA.get(Calendar.MONTH) == cal.get(Calendar.MONTH);
            }).collect(Collectors.toList());
            if (!userStream.isEmpty()) {
                UserActivityModel jm = userStream.get(0);
                jm.addQuantity();
                System.out.println("Added qty");
            } else {
                System.out.println("Not Added qty");
            }
        });


        return activityList;
    }

    static int count = 0;

    @Override
    public List<UserActivityModel> getLspOn_boarding() {

        List<UserActivityModel> activityList = new ArrayList<>();
        List<Models.AppUser> allLsps = getAllUsers(new UserPredicate(null, AppRolesEnum.ROLE_SERVICE_PROVIDER.name()), PageRequest.of(0, Integer.MAX_VALUE)).getContent();


        allLsps.forEach(c -> {
            Date date = c.getCreatedAt();
            count++;
            date.setMinutes(0);
            date.setHours(0);
            date.setSeconds(0);

            if (activityList.stream().filter(a -> {

                System.out.println(a.getDate().getYear() +" is equal to "+ date.getYear() + " and "+ a.getDate().getMonth() +" is equal to"+ date.getMonth());

                return a.getDate().getYear() == date.getYear() && a.getDate().getMonth() == date.getMonth();
            }).findFirst().isEmpty()) {
                activityList.add(new UserActivityModel(date));
                System.out.println("adding date " + date + "for "+c.getUsername());
            } else {
                System.out.println("already added " + date + "for "+c.getUsername());
            }

        });

        allLsps.forEach(c -> {

            Date date = c.getCreatedAt();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            System.out.println("time cal : "+cal.getTime() + " c time : "+date);

            cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),0,0,0,0);

            List<UserActivityModel> userStream = activityList.stream().filter(a -> {
                Calendar calA = Calendar.getInstance();
                calA.setTime(a.getDate());
                calA.set(calA.get(Calendar.YEAR),calA.get(Calendar.MONTH),0,0,0,0);

                System.out.println(c.getUsername() + " belongs in "+ a.getDate() + " with "+date);

                return calA.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && calA.get(Calendar.MONTH) == cal.get(Calendar.MONTH);
            }).collect(Collectors.toList());
            if (!userStream.isEmpty()) {
                UserActivityModel jm = userStream.get(0);
                jm.addQuantity();
                System.out.println("Added qty");
            } else {
                System.out.println("Not Added qty");
            }
        });

        return activityList;
    }

    @Override
    public void defaults() throws Exception {

        //chats
        try {
            notificationService.deleteAllChats();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //permissions
        Set<String> newP = Arrays.stream(AppUserPermission.values()).map(AppUserPermission::getPermission).collect(Collectors.toSet());

        //userService.savePermissionList(newP);
        newP.forEach(p -> {
            Models.Permissions permissions = saveAPermission(new Models.Permissions(p));
            log.info("Saved " + ConvertToJson.setJsonString(permissions));
        });
        Thread.sleep(2000);

        //roles
        Set<String> roles = Arrays.stream(AppRolesEnum.values()).map(Enum::name).collect(Collectors.toSet());
        List<RoleCreationForm> roleCreationFormSet = new ArrayList<>();
        final int[] c = {0};
        roles.forEach(r -> {
            Set<String> permissionsList = Enum.valueOf(AppRolesEnum.class, r).getGrantedAuthorities().stream().filter(i -> !Objects.equals(i, DataOps.getGrantedAuthorityRole(r))).map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());
            RoleCreationForm roleCreationForm = new RoleCreationForm(r, permissionsList);
            roleCreationFormSet.add(roleCreationForm);
            try {
                Models.AppRole updatedRole = saveANewRole(roleCreationForm);
                log.info("updated" + ConvertToJson.setJsonString(updatedRole));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("form " + Arrays.toString(c) + " : " + roleCreationForm.getPermissions().size() + " :: " + ConvertToJson.setJsonString(roleCreationForm));
            c[0]++;
        });

        System.out.println("role list " + roleCreationFormSet.size());


        Models.Service plumbing = serviceService.saveService(new ServiceRequestForm(PLUMBING, "General handling of pipes and water"));
        Models.Service electrical = serviceService.saveService(new ServiceRequestForm(ELECTRICAL, "General handling of wires, electrical equipment and wiring"));
        Models.Service mechanic = serviceService.saveService(new ServiceRequestForm(MECHANICAL, "General handling of mechanical equipment, cars, engines and technical appliances"));

        Models.Service laundry = serviceService.saveService(new ServiceRequestForm(LAUNDRY, "General handling of clothes, cleaning, ironing , carpet cleaning"));
        Models.Service gardening = serviceService.saveService(new ServiceRequestForm(GARDENING, "General handling of flowers, planting, maintaining and advice "));
        Models.Service cleaning = serviceService.saveService(new ServiceRequestForm(CLEANING, "General handling of cleaning; cars, house, clothes and compound ..."));

        Models.Service paintJob = serviceService.saveService(new ServiceRequestForm(PAINT_JOB, "General painting of houses, cars, walls and art"));
        Models.Service moving = serviceService.saveService(new ServiceRequestForm(MOVING, "General moving from houses or offices"));
        Models.Service repairs = serviceService.saveService(new ServiceRequestForm(GENERAL_REPAIRS, "General repairing of house hold items or personal equipment not limited"));


        //List<Models.AppRole> roles = userService.getAllRoles();

        //  Set<String> permissionsList = Enum.valueOf(AppRolesEnum.class, "ROLE_ADMIN").getGrantedAuthorities().stream().filter(i -> !Objects.equals(i, DataOps.getGrantedAuthorityRole("ROLE_ADMIN"))).map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());


//            Models.AppRole role = userService.saveANewRole(new RoleCreationForm("ROLE_DELETED_1", Set.of("role:delete", "role:create", "role:update", "role:read")));

        Models.AppRole newRole = saveANewRole(new RoleCreationForm("ROLE_DELETED", Set.of("job:delete")));

        Models.AppRole updatedRole = addPermissionListToARole(newRole.getName(), Set.of("role:delete", "role:create", "role:update", "role:read", "job:delete"));

        log.info("Role is " + ConvertToJson.setJsonString(updatedRole));

        //Models.Permissions permissions = userService.findByPermissionName("role:create").orElse(null);
        //log.info(ConvertToJson.setJsonString(permissions));

        // Models.AppRole role1 = userService.addPermissionListToARole(role.getName(),permissionsList);

        //log.info("Role 1 is "+ConvertToJson.setJsonString(role1));
        //User

        LinkedHashMap<String, String> preferredWorkingHours = new LinkedHashMap<>();
        preferredWorkingHours.put("Monday", "0800-2000");
        preferredWorkingHours.put("Tuesday", "0800-2000");

        LinkedList<String> specialities = new LinkedList<>();
        specialities.add(PLUMBING);
        specialities.add(CLEANING);


        NewUserForm admin = new NewUserForm("admin", "admin", "admin@gmail.com", "admin", "+2547xxx4xxx", "xxxxxxxxx", "I am admin", new LinkedHashMap<>(), new LinkedList<>(), AppRolesEnum.ROLE_ADMIN.name());
        NewUserForm client = new NewUserForm("client", "client", "client@gmail.com", "client", "+2547xXx6xXx", "xxxxxxxxx", "I am client", new LinkedHashMap<>(), new LinkedList<>(), AppRolesEnum.ROLE_CLIENT.name());
        NewUserForm bruce = new NewUserForm("bruce", "bruc3balo", "bruce@gmail.com", "qwerty", "+254702688714", "34945663", "I am moi", new LinkedHashMap<>(), new LinkedList<>(), AppRolesEnum.ROLE_CLIENT.name());
        NewUserForm provider = new NewUserForm("provider", "provider", "provider@gmail.com", "provider", "+2547XxXxX6X", HY, HY, preferredWorkingHours, specialities, AppRolesEnum.ROLE_SERVICE_PROVIDER.name());


        NewUserForm provider2 = new NewUserForm("provider2", "provider2", "provider2@gmail.com", "provider2", "+2547XxX45xx", HY, HY, preferredWorkingHours, specialities, AppRolesEnum.ROLE_SERVICE_PROVIDER.name());

        Models.AppUser adminUser = saveAUser(admin);
        Models.AppUser clientUser = saveAUser(client);
        Models.AppUser providerUser = saveAUser(provider);
        Models.AppUser bruceUser = saveAUser(bruce);

        updateAUser(admin.getUsername(), new UserUpdateForm(true));
        updateAUser(client.getUsername(), new UserUpdateForm(true));
        updateAUser(provider.getUsername(), new UserUpdateForm(true));
        updateAUser(bruceUser.getUsername(), new UserUpdateForm(true));

        specialities.add(GENERAL_REPAIRS);
        specialities.add(LAUNDRY);
        specialities.add(MECHANICAL);
        Models.AppUser provider2User = saveAUser(provider2);
        updateAUser(provider2User.getUsername(), new UserUpdateForm(true));
        //updateAUser(provider2.getUsername(), new UserUpdateForm(true));
    }
}
