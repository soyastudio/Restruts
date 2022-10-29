package soya.framework.action.dispatch;

import com.google.gson.*;
import soya.framework.action.*;
import soya.framework.commons.util.ReflectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@ActionDefinition(domain = "dispatch",
        name = "generic-method-dispatch",
        path = "/dispatch-to-method",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Method Dispatch",
        description = {
                "Dispatch to method of service from context or static method."
        })
public class GenericMethodDispatchAction extends GenericDispatchAction<Object> {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "c",
            description = "Class name or service name."
    )
    private String className;

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "m",
            description = {
                    "Method name or signature. If only name provided, it means the class has only one public method of this name or the method parameter number is zero. Otherwise, method signature need provided. Examples are:",
                    "- toString",
                    "- myMethod()",
                    "- anotherMethod(java.lang.String, java.util.Date)"
            }

    )
    private String method;

    @Override
    public Object execute() throws Exception {

        Class<?> cls = Class.forName(className);
        Method method = getMethod(cls, this.method);

        Object impl = Modifier.isStatic(method.getModifiers()) ? null : ActionContext.getInstance().getService(cls);

        Class[] paramTypes = method.getParameterTypes();
        Object[] paramValues = new Object[paramTypes.length];

        if (paramTypes.length == 1) {
            paramValues[0] = ConvertUtils.convert(data, paramTypes[0]);

        } else if (paramTypes.length > 1 && data != null) {
            JsonElement jsonElement = JsonParser.parseString(data);
            if (!jsonElement.isJsonArray()) {
                throw new IllegalArgumentException("Json array is expected.");
            }

            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (jsonArray.size() != paramTypes.length) {
                throw new IllegalArgumentException("The size of json array does not match the method parameter size.");
            }

            for (int i = 0; i < paramTypes.length; i++) {
                paramValues[i] = gson.fromJson(jsonArray.get(i), paramTypes[i]);
            }
        }

        return method.invoke(impl, paramValues);
    }

    private Method getMethod(Class<?> cls, String signature) throws ClassNotFoundException, NoSuchMethodException {
        String name = signature.trim();
        Class<?>[] paramTypes = new Class<?>[0];
        if (name.indexOf('(') > 0 && name.endsWith(")")) {
            int sep = name.indexOf('(');
            String[] arr = name.substring(sep + 1, name.length() - 1).split(",");
            paramTypes = new Class[arr.length];
            for (int i = 0; i < arr.length; i++) {
                paramTypes[i] = Class.forName(arr[i].trim());
            }

            name = name.substring(0, sep);

        } else {
            Method[] methods = ReflectUtils.findMethods(cls, name);
            if (methods.length == 1) {
                return methods[0];
            }
        }

        return cls.getMethod(name, paramTypes);

    }

}
