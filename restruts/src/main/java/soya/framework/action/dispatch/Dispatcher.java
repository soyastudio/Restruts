package soya.framework.action.dispatch;

import soya.framework.action.ActionCallable;

import java.lang.reflect.Method;

public abstract class Dispatcher {

    protected Class<?> executeClass;
    protected String methodName;
    protected Class<?>[] parameterTypes = new Class[0];

    public Dispatcher(Class<?> executeClass, String methodName, Class<?>[] parameterTypes) {
        this.executeClass = executeClass;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
    }

    public abstract Object dispatch(ActionCallable context) throws Exception;

    protected Method method() throws NoSuchMethodException {
        return executeClass.getMethod(methodName, parameterTypes);
    }


}
