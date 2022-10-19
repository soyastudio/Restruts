package soya.framework.action.actions;

import soya.framework.action.Action;

public abstract class DispatchAction<S, T> extends Action<T> {

    private Class<S> serviceType;

    public DispatchAction() {

    }

    @Override
    public T execute() throws Exception {
        return null;
    }

    protected abstract String getMethodName();

    protected String[] getParameterNames() {
        return new String[0];
    }
 }
