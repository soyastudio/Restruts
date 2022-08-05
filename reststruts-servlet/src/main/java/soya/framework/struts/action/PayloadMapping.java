package soya.framework.struts.action;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PayloadMapping {
    String[] consumes();

    String description() default "";
}
