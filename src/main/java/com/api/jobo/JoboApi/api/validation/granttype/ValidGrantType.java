package com.api.jobo.JoboApi.api.validation.granttype;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


@Documented
@Constraint(validatedBy = ValidGrantTypeValidator.class)
@Target({ANNOTATION_TYPE, FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGrantType {
    String message() default "Valid Grant Type required";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
