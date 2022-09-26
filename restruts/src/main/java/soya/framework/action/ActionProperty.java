package soya.framework.action;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionProperty {

    String name() default "";

    String option() default "";

    int displayOrder() default 5;

    String[] description() default {};

    boolean required() default false;

    String defaultValue() default "";

    PropertyType parameterType();

    String contentType() default MediaType.TEXT_PLAIN;

    enum PropertyType {
        PATH_PARAM, QUERY_PARAM, HEADER_PARAM, COOKIE_PARAM, FORM_PARAM, MATRIX_PARAM, BEAN_PARAM, PAYLOAD;

        private static final PropertyType[] SEQUENCE
                = {PATH_PARAM, QUERY_PARAM, HEADER_PARAM, COOKIE_PARAM, FORM_PARAM, MATRIX_PARAM, BEAN_PARAM, PAYLOAD};

        public static final int index(PropertyType type) {
            int i = 0;
            for (PropertyType p : SEQUENCE) {
                if (p.equals(type)) {
                    return i;
                } else {
                    i++;
                }
            }

            return -1;
        }
    }

}
