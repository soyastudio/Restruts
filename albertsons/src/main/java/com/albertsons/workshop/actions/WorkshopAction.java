package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Workspace;
import soya.framework.action.Action;
import soya.framework.action.WiredService;

public abstract class WorkshopAction<T> extends Action<T> {

    @WiredService
    protected Workspace workspace;
}
