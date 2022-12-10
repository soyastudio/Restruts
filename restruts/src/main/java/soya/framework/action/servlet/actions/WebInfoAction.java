package soya.framework.action.servlet.actions;

import soya.framework.action.Action;
import soya.framework.action.WiredService;

import javax.servlet.ServletContext;

public abstract class WebInfoAction<T> extends Action<T> {

    @WiredService
    protected ServletContext servletContext;
}
