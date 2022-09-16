package soya.framework.action.dispatch;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import soya.framework.action.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ActionProxyBuilder<T> {

    private final Class<T> proxyInterface;

    public ActionProxyBuilder(Class<T> proxyInterface) {
        this.proxyInterface = proxyInterface;

    }

    public T create() throws ActionProxyBuildException {
        if (!proxyInterface.isInterface()) {
            throw new ActionProxyBuildException("Class is not an interface: " + proxyInterface.getName());
        }

        ActionProxy actionProxy = proxyInterface.getAnnotation(ActionProxy.class);
        if (actionProxy == null) {
            throw new ActionProxyBuildException("Class is not annotated as 'ActionProxy': " + proxyInterface.getName());
        }

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{proxyInterface});

        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {

            Parameter[] parameters = method.getParameters();
            Map<String, Integer> paramIndex = new LinkedHashMap<>();
            int index = 0;
            for (Parameter parameter : parameters) {
                ParamName paramName = parameter.getAnnotation(ParamName.class);
                if (paramName == null) {
                    throw new IllegalArgumentException("");
                }

                paramIndex.put(paramName.value(), index);
                index++;
            }

            ActionMapping actionMapping = method.getAnnotation(ActionMapping.class);
            ActionClass actionClass = ActionContext.getInstance().getActionMappings().actionClass(ActionName.fromURI(URI.create(actionMapping.uri())));
            ActionCallable action = actionClass.newInstance();

            for (ActionParameter ap : actionMapping.parameters()) {
                Object value = null;
                if (AssignmentMethod.VALUE.equals(ap.assignmentMethod())) {
                    value = ap.expression();

                } else if (AssignmentMethod.ENVIRONMENT.equals(ap.assignmentMethod())) {
                    value = ActionContext.getInstance().getProperty(ap.expression());

                } else if (AssignmentMethod.REFERENCE.equals(ap.assignmentMethod())) {

                } else if (AssignmentMethod.PARAMETER.equals(ap.assignmentMethod())) {
                    value = args[paramIndex.get(ap.expression())];
                }

                if(value != null) {
                    Field field = actionClass.getActionField(ap.name());
                    field.setAccessible(true);
                    field.set(action, ConvertUtils.convert(value, field.getType()));
                }

            }

            Object result = action.call().get();
            if (method.getReturnType() != Void.TYPE) {
                return ConvertUtils.convert(result, method.getReturnType());

            } else {
                return null;
            }

        });

        return (T) enhancer.create();
    }

    static class Context {
        private Method method;
        private Object[] args;

        public Context(Method method, Object[] args) {
            this.method = method;
            this.args = args;
        }
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
