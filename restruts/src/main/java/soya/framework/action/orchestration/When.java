package soya.framework.action.orchestration;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface When {
    String condition();

    String dispatch();
}
