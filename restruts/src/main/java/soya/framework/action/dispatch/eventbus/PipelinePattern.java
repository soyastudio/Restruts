package soya.framework.action.dispatch.eventbus;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PipelinePattern {
    Task[] tasks();
}
