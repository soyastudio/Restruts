package soya.framework.action.dispatch;

import soya.framework.action.Action;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionMapping {
    Class<? extends Action> actionType();

    ActionParameterSetting[] parameterSettings() default {};

}
