package soya.framework.action.dispatch.eventbus;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventBusPattern {

    EventSubscriber[] subscribers();

}
