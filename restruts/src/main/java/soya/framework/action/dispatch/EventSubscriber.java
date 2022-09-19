package soya.framework.action.dispatch;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventSubscriber {

    String subscribe();

    String publish() default "";

    ActionDispatchPattern dispatch();

}
