package soya.framework.quartz.actions;

import soya.framework.action.Domain;

@Domain(
        name = "quartz-scheduler",
        path = "/scheduler/quartz",
        title = "Quartz Scheduler",
        description = "Quartz Scheduler extensions and services."
)
public interface QuartzSchedulerDomain {

}
