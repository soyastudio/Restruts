package soya.framework.action.dispatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.common.util.ReflectUtils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@ActionDefinition(domain = "dispatch",
        name = "generic-command-dispatch",
        path = "/dispatch/command",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Command Dispatch",
        description = "This action dispatches execution to an executing class which using **Command Design Pattern**. The execution method take zero argument, and parameter values are set through bean properties or fields.")
public class GenericCommandDispatchAction extends Action<Object> {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            description = "Command class full name.")
    private String className;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            description = "Execution method. The method must take no arguments.")
    private String methodName;

    @ActionProperty(parameterType = ActionProperty.PropertyType.PAYLOAD,
            contentType = MediaType.APPLICATION_JSON,
            description = "Parameter values in json format.")
    private String payload;

    @Override
    public Object execute() throws Exception {
        Class<?> cls = Class.forName(className);
        Map<String, PropertyDescriptor> propertyDescriptorMap = new HashMap<>();
        for (PropertyDescriptor ppt : Introspector.getBeanInfo(cls).getPropertyDescriptors()) {
            propertyDescriptorMap.put(ppt.getName(), ppt);
        }

        Method method = ReflectUtils.findMethod(cls, methodName);

        Object instance = cls.newInstance();
        if (payload != null) {
            JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
            jsonObject.entrySet().forEach(e -> {
                try {
                    if (propertyDescriptorMap.containsKey(e.getKey())) {
                        PropertyDescriptor ppt = propertyDescriptorMap.get(e.getKey());
                        Object value = gson.fromJson(e.getValue(), ppt.getPropertyType());
                        if (ppt.getWriteMethod() != null && Modifier.isPublic(ppt.getWriteMethod().getModifiers())) {
                            Method writer = ppt.getWriteMethod();
                            writer.invoke(instance, new Object[]{value});
                        }

                    } else {
                        Field field = ReflectUtils.findField(cls, e.getKey());
                        field.setAccessible(true);
                        field.set(instance, gson.fromJson(e.getValue(), field.getType()));
                    }
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            });
        }

        return method.invoke(instance, new Object[0]);
    }
}
