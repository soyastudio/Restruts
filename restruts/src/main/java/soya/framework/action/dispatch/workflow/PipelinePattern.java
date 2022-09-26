package soya.framework.action.dispatch.workflow;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PipelinePattern {
    Task[] tasks();
}