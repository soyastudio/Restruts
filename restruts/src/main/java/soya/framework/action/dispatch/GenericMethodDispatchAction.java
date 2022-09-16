package soya.framework.action.dispatch;

import com.google.gson.*;
import soya.framework.action.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@ActionDefinition(domain = "dispatch",
        name = "generic-method-dispatch",
        path = "/dispatch/method-dispatch",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Method Dispatch",
        description = {
                "Dispatch to method of service from context or static method."
        })
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
        Class<?> cls = Class.forName(className);
        Class<?>[] paramTypes = new Class[0];
        Method method = null;

        if (parameterTypes != null) {
            String[] arr = parameterTypes.split(",");
            paramTypes = new Class[arr.length];
            for (int i = 0; i < arr.length; i++) {
                paramTypes[i] = Class.forName(arr[i].trim());
            }
            try {
                method = cls.getMethod(methodName, paramTypes);

            } catch (Exception ex) {
                throw new IllegalArgumentException(ex);
            }

        } else {
            method = findMethod(cls, methodName);
            if (method != null) {
                paramTypes = method.getParameterTypes();
            }
        }

        if (method == null) {
            throw new IllegalArgumentException("Cannot find method '" + methodName + "' for class: " + className);
        }

        Object[] paramValues = new Object[paramTypes.length];

        if (payload != null) {
            JsonElement jsonElement = JsonParser.parseString(payload);
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                if (parameterTypes.length() != jsonArray.size()) {
                    throw new IllegalArgumentException("Cannot parse payload against method parameter size: json array size should be " + paramTypes.length);

                } else {
                    for (int i = 0; i < paramTypes.length; i++) {
                        paramValues[i] = gson.fromJson(jsonArray.get(i), paramTypes[i]);
                    }
                }
            } else if (paramTypes.length == 1) {
                paramValues[0] = gson.fromJson(jsonElement, paramTypes[0]);

            } else {
                throw new IllegalArgumentException("Cannot parse payload against method parameter: json array is required.");
            }
        }

        if (Modifier.isStatic(method.getModifiers())) {
            return method.invoke(null, paramValues);

        } else {
            Object impl = ActionContext.getInstance().getService(cls);
            return method.invoke(impl, paramTypes);
        }
    }

    private Method findMethod(Class<?> cls, String methodName) {
        Class parent = cls;
        while (!parent.getName().equals("java.lang.Object")) {
            List<Method> methodList = new ArrayList<>();
            for (Method method : parent.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && Modifier.isPublic(method.getModifiers())) {
                    methodList.add(method);
                }
            }

            if (methodList.size() == 1) {
                return methodList.get(0);
            } else if (methodList.size() > 1) {
                throw new IllegalArgumentException("There are " + methodList.size() + " methods named as '" + methodName + "' for class: " + className);
            }

            parent = cls.getSuperclass();
        }

        return null;
    }
}
