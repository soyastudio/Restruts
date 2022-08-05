package soya.framework.struts.action;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyMapping {
    String value();

    ParameterType parameterType();

    String description() default "";

    enum ParameterType {
        BEAN_PARAM, COOKIE_PARAM, FORM_PARAM, HEADER_PARAM, MATRIX_PARAM, PATH_PARAM, QUERY_PARAM;
    }

}
