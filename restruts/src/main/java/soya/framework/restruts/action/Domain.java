package soya.framework.restruts.action;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Domain {
    String name();

    String title() default "";

    String description() default "";
}
