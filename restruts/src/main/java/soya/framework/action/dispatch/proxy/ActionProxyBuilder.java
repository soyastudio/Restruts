package soya.framework.action.dispatch.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import soya.framework.action.*;
import soya.framework.action.dispatch.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class ActionProxyBuilder<T> {

    private final Class<T> proxyInterface;

    public ActionProxyBuilder(Class<T> proxyInterface) throws ActionProxyBuildException {
        this.proxyInterface = proxyInterface;
        validateInterface(proxyInterface);
    }

    public T create() {
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

            ActionDispatchPattern actionDispatchPattern = method.getAnnotation(ActionDispatchPattern.class);
            ActionDispatch actionDispatch = ActionDispatch.fromURI(actionDispatchPattern.uri());
            ActionClass actionClass = ActionClass.get(actionDispatch.getActionName());

            ActionCallable action = actionClass.newInstance();
            for (Field field : actionClass.getActionFields()) {
                Assignment assignment = actionDispatch.getAssignment(field.getName());
                Object value = null;
                if (AssignmentType.VALUE.equals(assignment.getAssignmentType())) {
                    value = assignment.getExpression();

                } else if (AssignmentType.RESOURCE.equals(assignment.getAssignmentType())) {
                    value = Resources.getResourceAsString(assignment.getExpression());

                } else if (AssignmentType.REFERENCE.equals(assignment.getAssignmentType())) {
                    // TODO:

                } else if (AssignmentType.PARAMETER.equals(assignment.getAssignmentType())) {
                    value = args[paramIndex.get(assignment.getExpression())];

                }

                if (value != null) {
                    field.setAccessible(true);
                    field.set(action, ConvertUtils.convert(value, field.getType()));
                }

            }

            ActionResult actionResult = action.call();
            if (actionDispatch.getFragment() != null) {
                actionResult = Fragment.process(actionResult, actionDispatch.getFragment());
            }

            Object result = actionResult.get();
            if (method.getReturnType() != Void.TYPE) {
                return ConvertUtils.convert(result, method.getReturnType());

            } else {
                return null;
            }

        });

        return (T) enhancer.create();
    }

    private void validateInterface(Class<T> proxyInterface) throws ActionProxyBuildException {

        if (!proxyInterface.isInterface()) {
            throw new ActionProxyBuildException("Class is not an interface: " + proxyInterface.getName());
        }

        ActionProxyPattern actionProxy = proxyInterface.getAnnotation(ActionProxyPattern.class);
        if (actionProxy == null) {
            throw new ActionProxyBuildException("Class is not annotated as 'ActionProxy': " + proxyInterface.getName());
        }

        for (Method method : proxyInterface.getDeclaredMethods()) {
            ActionDispatchPattern actionDispatchPattern = method.getAnnotation(ActionDispatchPattern.class);
            ActionDispatch actionDispatch = ActionDispatch.fromURI(actionDispatchPattern.uri());
            ActionClass actionClass = ActionClass.get(actionDispatch.getActionName());
            if (actionClass == null) {
                throw new ActionProxyBuildException("ActionClass is not found: " + actionDispatch.getActionName());
            }

            Parameter[] parameters = method.getParameters();
            String[] parameterNames = actionDispatch.getParameterNames();
            if (parameters.length != parameterNames.length) {
                throw new ActionProxyBuildException("The number of dispatch parameters does not match that of method parameters.");
            }

            if (parameters.length > 0) {
                Set<String> set = new HashSet<>();
                for (Parameter parameter : parameters) {
                    set.add(parameter.getAnnotation(ParamName.class).value());
                }

                for (String paramName : parameterNames) {
                    if (!set.contains(paramName)) {
                        throw new ActionProxyBuildException("Parameter '" + paramName +"' is not defined in method: " + proxyInterface.getName() + "." + method.getName());

                    }
                }

            }
        }
    }
}
