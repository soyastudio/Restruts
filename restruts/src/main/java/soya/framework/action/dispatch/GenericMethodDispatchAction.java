package soya.framework.action.dispatch;

import com.google.gson.*;
import soya.framework.action.*;
import soya.framework.action.dispatch.evaluators.JsonPayloadEvaluator;
import soya.framework.common.util.ReflectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@ActionDefinition(domain = "dispatch",
        name = "generic-method-dispatch",
        path = "/dispatch/method",
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

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, displayOrder = 6)
    private String assignments;

    @ActionProperty(parameterType = ActionProperty.PropertyType.PAYLOAD, contentType = MediaType.TEXT_PLAIN)
    private String payload;

    @Override
    public Object execute() throws Exception {
        Class<?> cls = Class.forName(className);
        Method method = null;

        Method[] methods = ReflectUtils.findMethods(cls, methodName);
        if (methods.length == 0) {
            throw new NoSuchMethodException("Cannot find method '" + methodName + "' for class: " + className);

        } else if (methods.length == 1) {
            method = methods[0];

        }

        Object impl = Modifier.isStatic(method.getModifiers()) ? null : ActionContext.getInstance().getService(cls);

        Class[] paramTypes = method.getParameterTypes();
        Object[] paramValues = new Object[paramTypes.length];

        if (assignments != null) {
            JsonElement jsonElement = payload == null ? JsonNull.INSTANCE : JsonParser.parseString(payload);
            Evaluator<JsonElement> evaluator = new JsonPayloadEvaluator();

            String[] arr = assignments.split(",");
            if (arr.length != paramTypes.length) {
                throw new IllegalArgumentException();
            }

            for (int i = 0; i < paramTypes.length; i++) {
                paramValues[i] = new Assignment(arr[i].trim()).evaluate(jsonElement, evaluator, paramTypes[i]);

            }
        } else if (paramTypes.length == 1) {
            paramValues[0] = ConvertUtils.convert(payload, paramTypes[0]);
        }

        return method.invoke(impl, paramValues);
    }

}
