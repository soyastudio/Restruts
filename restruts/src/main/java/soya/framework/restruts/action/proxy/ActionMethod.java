package soya.framework.restruts.action.proxy;

import soya.framework.restruts.action.Action;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionMethod {
    Class<? extends Action> actionType();

    ActionParameterSetting[] parameterSettings() default {};

}
