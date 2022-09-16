package soya.framework.action.dispatch;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionMapping {
    String uri();

    ActionParameter[] parameters() default {};

}
