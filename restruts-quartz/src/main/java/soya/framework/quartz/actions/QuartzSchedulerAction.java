package soya.framework.quartz.actions;

import soya.framework.action.Action;
import soya.framework.action.ServiceWired;
import soya.framework.quartz.QuartzSchedulerManager;

public abstract class QuartzSchedulerAction<T> extends Action<T> {

    @ServiceWired
    protected QuartzSchedulerManager schedulerManager;

}
