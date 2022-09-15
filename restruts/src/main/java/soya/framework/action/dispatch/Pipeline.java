package soya.framework.action.dispatch;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pipeline {
    Task[] tasks();

}
