package soya.framework.action.dispatch.proxy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import soya.framework.action.*;
import soya.framework.action.dispatch.ParamName;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@ActionDefinition(domain = "dispatch",
        name = "proxy-invoke",
        path = "/proxy/invoke",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class ProxyInvokeAction extends Action<String> {

    @ActionProperty(
            description = {
            },
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            option = "c")
    private String className;

    @ActionProperty(
            description = {
            },
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            option = "m")
    private String methodName;

    @ActionProperty(
            description = {
            },
            parameterType = ParameterType.PAYLOAD,
            contentType = MediaType.APPLICATION_JSON,
            option = "i")
    private String inputs;

    @WiredService
    private ActionProxyFactory factory;

    @Override
    public String execute() throws Exception {
        Class<?> proxyInterface = Class.forName(className);
        Object implementation = factory.create(proxyInterface);

        Method method = null;
        for(Method m : proxyInterface.getDeclaredMethods()) {
            if(m.getName().equals(methodName)) {
                method = m;
                break;
            }
        }

        if(method == null) {
            throw new IllegalArgumentException("Method '" + methodName + "' does not exist in proxy interface '" + className + "'.");
        }

        JsonObject jsonObject = inputs == null? new JsonObject() : JsonParser.parseString(inputs).getAsJsonObject();

        Parameter[] parameters = method.getParameters();
        Object[] values = new Object[parameters.length];

        for(int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String paramName = parameter.getAnnotation(ParamName.class).value();
            Class<?> paramType = parameter.getType();

            values[i] = ConvertUtils.convert(jsonObject.get(paramName), paramType);

        }

        Object result = method.invoke(implementation, values);

        return result.toString();
    }
}
