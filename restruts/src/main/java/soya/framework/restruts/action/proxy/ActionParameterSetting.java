package soya.framework.restruts.action.proxy;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionParameterSetting {
    String name();

    String value();

}
