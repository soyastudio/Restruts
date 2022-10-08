package soya.framework.quartz;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScheduledDispatchPattern {
    String jobId();

    String triggerId();

    String cronExpression();

}
