package soya.framework.action.actions;

import soya.framework.action.Action;

public abstract class DispatchAction<T> extends Action<T> {

    @Override
    public T execute() throws Exception {
        return null;
    }

    protected abstract String getMethodName();

    protected String[] getParameterNames() {
        return new String[0];
    }
 }
