package soya.framework.action.dispatch;

import com.google.gson.*;
import org.apache.commons.beanutils.PropertyUtils;
import soya.framework.action.*;
import soya.framework.commons.util.ReflectUtils;
import soya.framework.commons.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Map;

@ActionDefinition(
        domain = "dispatch",
        name = "generic-command-dispatch",
        path = "/command-dispatch",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Command Dispatch",
        description = "Dispatch to an executing class which using **Command Design Pattern**. The execution method take zero argument, and parameter values are set through bean properties or fields."
)
public class GenericCommandDispatchAction extends GenericDispatchAction<Object> {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "u",
            description = {
                    "URI for command dispatch. The uri format is:",
                    "```",
                    "class://<className>/<methodName>?<queryString>",
                    "```",
                    "Query string format is like 'prop1=assign1(exp1)&prop2=assign2(exp2)'.",
                    "Here assign() function should be one of val(exp), res(exp), param(exp) or ref(exp):",
                    "- val(exp): directly assign property with string value from exp",
                    "- res(exp): extract contents from resource uri exp, such as 'classpath://kafka-config.properties'",
                    "- param(exp): evaluate value from payload input in json format using expression: exp"
            }
    )
    private String uri;

    @Override
    public Object execute() throws Exception {

        URI u = URI.create(uri);

        String className = u.getHost();
        Class<?> cls = Class.forName(className);

        String methodName = u.getPath();
        if (methodName.startsWith("/")) {
            methodName = methodName.substring(1);
        }
        Method method = cls.getMethod(methodName, new Class[0]);
        Object instance = cls.newInstance();

        String propertyAssignments = u.getQuery();

        if (propertyAssignments == null || propertyAssignments.trim().isEmpty()) {
            if (data != null) {
                JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
                jsonObject.entrySet().forEach(e -> {
                    String propName = e.getKey();
                    JsonElement jsonElement = e.getValue();

                    try {
                        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(instance, propName);
                        Object propValue = gson.fromJson(jsonElement, propertyDescriptor.getPropertyType());
                        PropertyUtils.setProperty(instance, propName, propValue);

                    } catch (Exception ex) {
                        Field field = ReflectUtils.findField(cls, propName);
                        field.setAccessible(true);
                        Object propValue = gson.fromJson(jsonElement, field.getType());
                        try {
                            field.set(instance, propValue);

                        } catch (IllegalAccessException exc) {
                            throw new RuntimeException(exc);
                        }
                    }
                });
            }

        } else {
            Map<String, List<String>> paramMap = StringUtils.splitQuery(propertyAssignments);
            JsonObject jsonObject = data == null ? null : JsonParser.parseString(data).getAsJsonObject();

            paramMap.entrySet().forEach(e -> {
                String propName = e.getKey();
                Class<?> propType = getPropertyType(propName, instance);
                Object propValue = null;

                Evaluation evaluation = new Evaluation(e.getValue().get(0));

                if (EvaluationMethod.VALUE.equals(evaluation.getAssignmentMethod())) {
                    propValue = ConvertUtils.convert(evaluation.getExpression(), propType);

                } else if (EvaluationMethod.RESOURCE.equals(evaluation.getAssignmentMethod())) {
                    propValue = ConvertUtils.convert(Resources.getResourceAsString(evaluation.getExpression()), propType);

                } else if (EvaluationMethod.PARAMETER.equals(evaluation.getAssignmentMethod())) {
                    if (jsonObject != null) {
                        JsonElement jsonElement = jsonObject.get(evaluation.getExpression());
                        propValue = gson.fromJson(jsonElement, propType);
                    }

                } else {
                    throw new IllegalArgumentException(evaluation.getAssignmentMethod() + " is not supported for command dispatch.");
                }

                if (propValue != null) {
                    setProperty(instance, propName, propValue);
                }
            });
        }

        Object result = method.invoke(instance, new Object[0]);
        if (result instanceof ActionResult) {
            result = ((ActionResult) result).get();
        }

        return result;
    }

    private Class<?> getPropertyType(String propName, Object bean) {
        try {
            PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(bean, propName);
            if (propertyDescriptor == null || propertyDescriptor.getWriteMethod() == null) {
                return getFieldType(propName, bean.getClass());

            } else {
                return propertyDescriptor.getPropertyType();

            }

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return getFieldType(propName, bean.getClass());
        }
    }

    private Class<?> getFieldType(String fieldName, Class<?> type) {
        Field field = ReflectUtils.findField(type, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Cannot find field: " + fieldName);
        }

        return field.getType();
    }

    private void setProperty(Object instance, String propName, Object propValue) {
        try {
            PropertyUtils.setProperty(instance, propName, propValue);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Field field = ReflectUtils.findField(instance.getClass(), propName);
            if (field == null) {
                throw new IllegalArgumentException("Cannot find field: " + propName);
            }

            field.setAccessible(true);
            try {
                field.set(instance, propValue);

            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
