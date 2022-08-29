package soya.framework.restruts.action.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import soya.framework.restruts.action.Action;
import soya.framework.restruts.action.ActionExecutor;
import soya.framework.restruts.action.ConvertUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public final class ActionProxyBuilder<T> {

    private final Class<T> proxyInterface;

    public ActionProxyBuilder(Class<T> proxyInterface) {
        this.proxyInterface = proxyInterface;

    }

    public T create() throws ActionProxyBuildException {
        if(!proxyInterface.isInterface()) {
            throw new ActionProxyBuildException("Class is not an interface: " + proxyInterface.getName());
        }

        ActionProxy actionProxy = proxyInterface.getAnnotation(ActionProxy.class);
        if(actionProxy == null) {
            throw new ActionProxyBuildException("Class is not annotated as 'ActionProxy': " + proxyInterface.getName());
        }

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{proxyInterface});

        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {

            ActionMethod actionMethod = method.getAnnotation(ActionMethod.class);
            Class<? extends Action> actionType = actionMethod.actionType();
            ActionExecutor executor = ActionExecutor.executor(actionType);

            for(ActionParameterSetting setting: actionMethod.parameterSettings()) {

                executor.setProperty(setting.name(), setting.value());
            }

            Parameter[] parameters = method.getParameters();
            for(int i = 0; i < parameters.length; i ++) {
                Parameter param = parameters[i];
                Object value = args[i];
                ActionParameter ap = param.getAnnotation(ActionParameter.class);
                executor.setProperty(ap.value(), value);
            }

            Object result = executor.execute();
            if(method.getReturnType() != Void.TYPE) {
                return ConvertUtils.convert(result, method.getReturnType());

            } else {
                return null;
            }

        });

        return (T)enhancer.create();
    }

    public static class ActionProxyBuildException extends RuntimeException {

        public ActionProxyBuildException(String message) {
            super(message);
        }

        public ActionProxyBuildException(String message, Throwable cause) {
            super(message, cause);
        }

        public ActionProxyBuildException(Throwable cause) {
            super(cause);
        }
    }

}