package soya.framework.action.actions;

import soya.framework.action.Action;

import java.lang.reflect.Field;

public abstract class DispatchAction<S, T> extends Action<T> {

    private Class<S> serviceType;

    protected String serviceMethod;

    public DispatchAction() {

    }

    @Override
    public T execute() throws Exception {
        Field[] fields = getActionClass().getActionFields();
        return null;
    }

    protected abstract String getMethodName();

    protected String[] getParameterNames() {
        return new String[0];
    }
 }
