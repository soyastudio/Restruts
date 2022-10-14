package soya.framework.quartz.actions;

import soya.framework.action.Action;
import soya.framework.action.ActionContext;
import soya.framework.quartz.QuartzSchedulerManager;

public abstract class QuartzSchedulerAction<T> extends Action<T> {

    protected QuartzSchedulerManager quartzSchedulerManager() {
        return ActionContext.getInstance().getService(QuartzSchedulerManager.class);
    }


}
