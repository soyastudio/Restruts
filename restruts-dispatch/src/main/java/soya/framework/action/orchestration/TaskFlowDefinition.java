package soya.framework.action.orchestration;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TaskFlowDefinition {

    String processor() default "soya.framework.action.orchestration.SequentialTaskFlowExecutor";

    TaskDefinition[] tasks();

    String resultHandler();
}
