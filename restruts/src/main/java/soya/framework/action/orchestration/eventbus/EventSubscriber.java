package soya.framework.action.orchestration.eventbus;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventSubscriber {
    String address();

}
