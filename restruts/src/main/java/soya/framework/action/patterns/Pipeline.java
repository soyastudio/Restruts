package soya.framework.action.patterns;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pipeline {
    Task[] tasks();

}
