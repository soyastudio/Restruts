package soya.framework.struts.action;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionMapping {
    String path();

    HttpMethod method();

    String[] produces();

    String description() default "";

    String externalLink() default "";

    enum HttpMethod {
        GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS;
    }
}
