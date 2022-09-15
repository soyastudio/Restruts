package soya.framework.action.dispatch;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Task {
    String name();

    String dispatch();

}
