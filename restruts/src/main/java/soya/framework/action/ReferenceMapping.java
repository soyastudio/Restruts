package soya.framework.action;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReferenceMapping {
    String name() default "";

    String defaultValue();

    boolean required() default true;

    String description() default "";
}
