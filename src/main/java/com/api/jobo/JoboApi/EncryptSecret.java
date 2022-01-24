package com.api.jobo.JoboApi;


import com.api.jobo.JoboApi.api.model.RoleCreationForm;
import com.api.jobo.JoboApi.config.security.AppRolesEnum;
import com.api.jobo.JoboApi.config.security.AppUserPermission;
import com.api.jobo.JoboApi.utils.ConvertDate;
import com.api.jobo.JoboApi.utils.ConvertToJson;
import com.api.jobo.JoboApi.utils.DataOps;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.api.jobo.JoboApi.utils.DataOps.*;


public class EncryptSecret {


    public static void main(String[] args) throws ParseException {

        //secret
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        System.out.println(passwordEncoder.encode("secret"));
        System.out.println(passwordEncoder.matches("secret", "$2a$10$l8Ffz3g/z0FHOFj3Op58eOdwK0kEINCgr9bZcrjz65ZJjgTxQCTdO"));

        Date date = ConvertDate.formatDate(formatLocalDateTime(LocalDateTime.now()), TIMESTAMP_PATTERN); //2020-05-08 23:17:22 PM
        System.out.println(date);

        Date date1 = ConvertDate.formatDate(formatLocalDate(LocalDate.now()), DATE_PATTERN);//2020-05-08
        System.out.println(date1);

        Set<String> newP = Arrays.stream(AppUserPermission.values()).map(AppUserPermission::getPermission).collect(Collectors.toSet());
        System.out.println(newP.size());

        Set<String> roles = Arrays.stream(AppRolesEnum.values()).map(Enum::name).collect(Collectors.toSet());
        System.out.println("role list " + roles);
        final int[] c = {0};
        roles.forEach(r -> {
            Set<String> permissionsList = Enum.valueOf(AppRolesEnum.class, r).getGrantedAuthorities().stream().filter(i -> !Objects.equals(i, DataOps.getGrantedAuthorityRole(r))).map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());
            RoleCreationForm roleCreationForm = new RoleCreationForm(r, permissionsList);
            System.out.println("form " + Arrays.toString(c) + " " + ConvertToJson.setJsonString(roleCreationForm));
            c[0]++;
        });


        String list = "\"DEV\"";
        String map = "{\"MONDAY\":\"8am - 8pm\",\"TUESDAY\":\"8am - 8pm\"}";
        System.out.println(list.replaceAll("[\\[\\]]", ""));
        System.out.println(map.replaceAll("[\\[\\]]", ""));

        String stringDate = "2021-12-22T12:12:13.000+00:00";
        System.out.println("DATE IS 1 : " + ConvertDate.formatLocalDateTime(stringDate));
        System.out.println("DATE IS 2 : " + ConvertDate.formatLocalDateTime(stringDate));
        System.out.println("DATE IS 3 : " + ConvertDate.formatDate(stringDate));


        Date startToday = ConvertDate.formatDate(formatLocalDateTime(LocalDateTime.now()), TIMESTAMP_PATTERN);
        startToday.setMinutes(0);
        startToday.setHours(0);
        startToday.setSeconds(0);
        Date endToday = ConvertDate.formatDate(formatLocalDateTime(LocalDateTime.now()), TIMESTAMP_PATTERN);
        endToday.setMinutes(59);
        endToday.setHours(23);
        endToday.setSeconds(59);

        System.out.println("start today is " + startToday);
        System.out.println("end today is " + endToday);

        String dae = "Wed Dec 29 13:38:12 EAT 2021";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("date is " + ConvertDate.formatDate(dae));


    }

}
