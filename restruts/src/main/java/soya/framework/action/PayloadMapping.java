package soya.framework.action;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PayloadMapping {
    String name() default "";

    boolean required() default true;

    String[] consumes();

    String description() default "";
}
