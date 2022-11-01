package soya.framework.action.orchestration.pipeline;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TaskDefinition {

    String name();

    String dispatch();

}