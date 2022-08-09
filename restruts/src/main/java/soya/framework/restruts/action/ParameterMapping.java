package soya.framework.restruts.action;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParameterMapping {

    ParameterType parameterType();

    String name() default "";

    String description() default "";

    enum ParameterType {
        COOKIE_PARAM, HEADER_PARAM, PATH_PARAM, QUERY_PARAM, FORM_PARAM, MATRIX_PARAM, BEAN_PARAM;

        private static final ParameterMapping.ParameterType[] SEQUENCE
                = {PATH_PARAM, QUERY_PARAM, HEADER_PARAM, COOKIE_PARAM, FORM_PARAM, MATRIX_PARAM, BEAN_PARAM};

        public static final int index(ParameterType type) {
            int i = 0;
            for (ParameterType p : SEQUENCE) {
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
