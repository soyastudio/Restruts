package soya.framework.action.dispatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@ActionDefinition(domain = "dispatch",
        name = "generic-command-dispatch",
        path = "/dispatch/command-dispatch",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Command Dispatch",
        description = "Print as markdown format.")
public class GenericCommandDispatchAction extends Action<Object> {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String className;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String methodName;

    @ActionProperty(parameterType = ActionProperty.PropertyType.PAYLOAD, contentType = MediaType.APPLICATION_JSON)
    private String payload;

    @Override
    public Object execute() throws Exception {
        Class<?> cls = Class.forName(className);
        Map<String, PropertyDescriptor> propertyDescriptorMap = new HashMap<>();
        for (PropertyDescriptor ppt : Introspector.getBeanInfo(cls).getPropertyDescriptors()) {
            propertyDescriptorMap.put(ppt.getName(), ppt);
        }


        Method method = cls.getMethod(methodName, new Class[0]);

        Object instance = cls.newInstance();
        if (payload != null) {
            JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
            jsonObject.entrySet().forEach(e -> {
                if(propertyDescriptorMap.containsKey(e.getKey())) {
                    PropertyDescriptor ppt = propertyDescriptorMap.get(e.getKey());
                    Object value = gson.fromJson(e.getValue(), ppt.getPropertyType());

                    try {
                        if(ppt.getWriteMethod() != null && Modifier.isPublic(ppt.getWriteMethod().getModifiers())) {
                            Method writer = ppt.getWriteMethod();
                            writer.invoke(instance, new Object[] {value});

                        } else {
                            Field field = cls.getField(e.getKey());
                            field.setAccessible(true);
                            field.set(instance, value);
                        }

                    } catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }
                }
            });
        }

        return method.invoke(instance, new Object[0]);
    }
}
