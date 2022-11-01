package soya.framework.action.orchestration.pipeline;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PipelinePattern {
    TaskDefinition[] tasks();
}
