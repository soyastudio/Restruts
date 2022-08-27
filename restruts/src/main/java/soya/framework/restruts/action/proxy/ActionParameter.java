package soya.framework.restruts.action.proxy;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionParameter {
    String value();
}
