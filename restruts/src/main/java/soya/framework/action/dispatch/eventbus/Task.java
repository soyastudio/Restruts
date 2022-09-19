package soya.framework.action.dispatch.eventbus;

import soya.framework.action.dispatch.ActionDispatchPattern;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Task {

    String name();

    ActionDispatchPattern dispatch();

}
