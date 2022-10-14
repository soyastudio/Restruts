package soya.framework.action.dispatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.apache.commons.beanutils.PropertyUtils;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.ActionResult;
import soya.framework.action.MediaType;
import soya.framework.common.util.ReflectUtils;
import soya.framework.common.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@ActionDefinition(
        domain = "dispatch",
        name = "generic-command-dispatch",
        path = "/command-dispatch",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Command Dispatch",
        description = "Dispatch to an executing class which using **Command Design Pattern**. The execution method take zero argument, and parameter values are set through bean properties or fields.")
public class GenericCommandDispatchAction extends GenericDispatchAction<Object> {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @ActionProperty(
            description = "Command class full name.",
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "c"
    )
    private String className;

    @ActionProperty(
            description = "Execution method. The method must take no arguments.",
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "m")
    private String methodName;

    @ActionProperty(
            description = {
                    "Command property assignments in query string format such as 'prop1=assign1(exp1)&prop2=assign2(exp2)'.",
                    "Here assign() function should be one of val(exp), res(exp), param(exp) or ref(exp):",
                    "- val(exp): directly assign property with string value from exp",
                    "- res(exp): extract contents from resource uri exp, such as 'classpath://kafka-config.properties'",
                    "- param(exp): evaluate value from payload input in json format using expression: exp",
                    "- ref(exp): evaluate value from context using expression: exp, available for multiple action dispatch patterns such as pipeline, eventbus etc."
            },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "a")
    private String propertyAssignments;

    @Override
    public Object execute() throws Exception {
        Class<?> cls = Class.forName(className);
        Method method = ReflectUtils.findMethod(cls, methodName);

        Object instance = cls.newInstance();
        Object context = data;
        if (propertyAssignments != null && data != null) {
            try {
                context = JsonParser.parseString(data);

            } catch (Exception e) {

            }
        }

        final Object ctx = context;

        if (propertyAssignments != null) {
            Map<String, List<String>> paramMap = StringUtils.splitQuery(propertyAssignments);
            paramMap.entrySet().forEach(e -> {
                String propName = e.getKey();
                PropertyDescriptor propDesc = null;
                Field field = null;
                Class<?> propType = null;

                try {
                    propDesc = PropertyUtils.getPropertyDescriptor(instance, e.getKey());
                    propType = propDesc.getPropertyType();

                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    field = ReflectUtils.findField(cls, propName);
                    propType = field.getType();
                }

                if (propType != null) {
                    Assignment assignment = new Assignment(e.getValue().get(0));
                    Object value = evaluate(assignment, propType, ctx);

                    if (propDesc != null && propDesc.getWriteMethod() != null) {
                        try {
                            propDesc.getWriteMethod().invoke(instance, propName, value);

                        } catch (IllegalAccessException | InvocationTargetException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        try {
                            field.set(instance, value);

                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }
            });
        }

        Object result = method.invoke(instance, new Object[0]);
        if(result instanceof ActionResult) {
            result = ((ActionResult)result).get();
        }

        return result;
    }
}
