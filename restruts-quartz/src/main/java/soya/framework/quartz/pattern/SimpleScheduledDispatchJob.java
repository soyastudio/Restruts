package soya.framework.quartz.pattern;

import soya.framework.quartz.ScheduledDispatchJob;
import soya.framework.quartz.ScheduledDispatchPattern;

@ScheduledDispatchPattern(
        jobId = "simple",
        triggerId = "cron://simple",
        cronExpression = "*/10 * * ? * *"
)
public class SimpleScheduledDispatchJob extends ScheduledDispatchJob {

}
