package soya.framework.action.servlet;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StreamHandler {
    String value();
}
