package soya.framework.action.mvc;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MvcDefinition {
    String[] from();

    String method();

    String[] to();

}
