package soya.framework.action.dispatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import soya.framework.action.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@ActionDefinition(domain = "dispatch",
        name = "generic-method-dispatch",
        path = "/dispatch/method-dispatch",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Method Dispatch",
        description = "Print as markdown format.")
public class GenericMethodDispatchAction extends Action<Object> {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String className;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String methodName;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM)
    private String parameterTypes;

    @ActionProperty(parameterType = ActionProperty.PropertyType.PAYLOAD, contentType = MediaType.APPLICATION_JSON)
    private String payload;

    @Override
    public Object execute() throws Exception {
        if (parameterTypes != null && payload == null) {
            throw new IllegalArgumentException("Payload value is required");
        }

        Class<?> cls = Class.forName(className);
        Class<?>[] paramTypes = new Class[0];
        Object[] paramValues = new Object[0];
        if (parameterTypes != null) {
            String[] arr = parameterTypes.split(",");
            JsonArray jsonArray = JsonParser.parseString(payload).getAsJsonArray();
            if (arr.length != jsonArray.size()) {
                throw new IllegalArgumentException("");
            }

            paramTypes = new Class[arr.length];
            paramValues = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                paramTypes[i] = Class.forName(arr[i].trim());
                paramValues[i] = gson.fromJson(jsonArray.get(i), paramTypes[i]);
            }
        }

        Method method = cls.getMethod(methodName, paramTypes);

        if (Modifier.isStatic(method.getModifiers())) {
            return method.invoke(null, paramValues);

        } else {
            Object impl = ActionContext.getInstance().getService(cls);
            return method.invoke(impl, paramTypes);
        }
    }
}
