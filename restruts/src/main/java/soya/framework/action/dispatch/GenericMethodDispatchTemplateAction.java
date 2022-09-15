package soya.framework.action.dispatch;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import soya.framework.action.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@ActionDefinition(domain = "dispatch",
        name = "generic-method-dispatch-template",
        path = "/dispatch/method-dispatch",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Method Dispatch Template",
        description = "Print as markdown format.")
public class GenericMethodDispatchTemplateAction extends Action<Object> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String className;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String methodName;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM)
    private String parameterTypes;


    @Override
    public Object execute() throws Exception {
       Class<?> cls = Class.forName(className);
        Class<?>[] paramTypes = new Class[0];
        Object[] paramValues = new Object[0];
        if (parameterTypes != null) {
            String[] arr = parameterTypes.split(",");

            paramTypes = new Class[arr.length];
            for (int i = 0; i < arr.length; i++) {
                paramTypes[i] = Class.forName(arr[i].trim());
            }
        }

        Method method = cls.getMethod(methodName, paramTypes);

        return new Object();
    }

}
