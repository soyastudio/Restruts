package soya.framework.quartz.actions;

import soya.framework.action.Action;
import soya.framework.action.WiredService;
import soya.framework.quartz.QuartzSchedulerManager;

public abstract class QuartzSchedulerAction<T> extends Action<T> {

    @WiredService
    protected QuartzSchedulerManager schedulerManager;

}
