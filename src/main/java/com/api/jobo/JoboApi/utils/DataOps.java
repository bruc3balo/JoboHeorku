package com.api.jobo.JoboApi.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.api.jobo.JoboApi.globals.GlobalVariables.HY;
import static com.api.jobo.JoboApi.globals.GlobalVariables.KEY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class DataOps {

    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss a";
    public static final DateTimeFormatter LDT_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter LD_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);


    public static List<String> filterRequestParams(HttpServletRequest request, List<String> knownParams) {
        Enumeration<String> query = request.getParameterNames();
        List<String> list = Collections.list(query);
        list.removeAll(knownParams);
        return list;
    }


    public static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper = mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper = mapper.findAndRegisterModules();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        return mapper;
    }

    public static OkHttpClient getHttpClient() {
        return new OkHttpClient().newBuilder().build();
    }

    public static MediaType getMediaTypeClient() {
        return MediaType.parse(APPLICATION_JSON_VALUE);
    }


    public static String getTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return simpleDateFormat.format(timestamp);
    }

    public static String getEncodedPassword(String businessShortCode, String passKey) {
        String password = businessShortCode + passKey + getTimestamp();
        byte[] bytesPassword = password.getBytes(StandardCharsets.ISO_8859_1);
        return Base64.getEncoder().encodeToString(bytesPassword);
    }

    public static String getBasicAuth(String consumerKey, String consumerSecret) throws UnsupportedEncodingException {
        String auth = consumerKey + ":" + consumerSecret;
        byte[] bytesAuth = auth.getBytes("ISO-8859-1");
        return Base64.getEncoder().encodeToString(bytesAuth);
    }

    public static Date getNowFormattedDate() throws ParseException {
        return ConvertDate.formatDate(formatLocalDate(LocalDate.now()), DATE_PATTERN);
    }

    public static Date getNowFormattedDate(LocalDate date) throws ParseException {
        return ConvertDate.formatDate(formatLocalDate(date), DATE_PATTERN);
    }

    public static Date getNowFormattedFullDate() throws ParseException {
        return Calendar.getInstance().getTime();
    }

    public static Date getSpecificFormattedFullDate(LocalDateTime dateTime) throws ParseException {
        return ConvertDate.formatDate(formatLocalDateTime(dateTime), TIMESTAMP_PATTERN);
    }

    public static String formatLocalDate(LocalDate ld) {
        return LD_FORMATTER.format(ld);
    }

    public static String formatLocalDateTime(LocalDateTime ldt) {
        return LDT_FORMATTER.format(ldt);
    }

    public static SimpleGrantedAuthority getGrantedAuthorityRole(String role) {
        return new SimpleGrantedAuthority(role);
    }

    public static boolean isNumeric(String text) {
        return StringUtils.isNumeric(text);
    }


    public static String exactStringValue(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    public static BigDecimal strToBigDecimal(String value) {
        try {
            return (value == null || value.trim().isEmpty() ? new BigDecimal("0") : ((new BigDecimal(value.trim()))));
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getTransactionId(String type) {
        return UUID.randomUUID().toString().concat(HY).concat(type);
    }




    public static Integer strToInteger(String value) {
        try {
            return (value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim()));
        } catch (Exception ex) {
            return null;
        }
    }


    public static String getStringFromList(LinkedList<String> specialities) {
       if (specialities.isEmpty()) {
           return "";
       } else {
           StringBuilder specString = new StringBuilder();
           for (String spec : specialities) {
               specString.append(',').append(spec);
           }
           return specString.substring(1);
       }
    }

    public static LinkedList<String> getListFromString(String specialityString) {
        if (specialityString.isEmpty() || specialityString.isBlank()) {
            return new LinkedList<>();
        } else {
            LinkedList<String> specialities = new LinkedList<>();
            String[] a = specialityString.split(",");
            Collections.addAll(specialities, a);
            return specialities;
        }
    }

    public static String getStringFromMap(LinkedHashMap<String,String> workingHoursMap) {
        if (!workingHoursMap.isEmpty()) {
            StringBuilder rs = new StringBuilder();

            workingHoursMap.forEach((day,time)-> {
                String spec = day.concat(KEY).concat(time);
                rs.append(',').append(spec);
            });

            return rs.substring(1);
        } else {
            return "";
        }
    }

    public static LinkedHashMap<String,String> getMapFromString(String workingString) {
        if (workingString.isBlank() || workingString.isEmpty()) {
            return new LinkedHashMap<>();
        } else {
            LinkedHashMap<String,String> workingHoursMap = new LinkedHashMap<>();
            String[] a = workingString.split(",");

            for (String item : a) {
                String key = item.split("\\^")[0];
                String val = item.split("\\^")[1];
                workingHoursMap.put(key,val);
            }

            // Collections.addAll(specialities, a);
            return workingHoursMap;
        }
    }



}
