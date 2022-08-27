package soya.framework.restruts.action;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.lang.reflect.Field;
import java.util.*;

public final class ActionExecutor {

    private Class<? extends Action> actionType;
    private Map<String, Field> fieldMap = new LinkedHashMap<>();
    private Action action;

    private ActionExecutor(Class<? extends Action> actionType) {
        this.actionType = actionType;

        List<Field> fields = new ArrayList<>();
        Set<String> fieldNames = new HashSet<>();
        Class<?> cls = actionType;
        while (!cls.getName().equals("java.lang.Object")) {
            for (Field field : cls.getDeclaredFields()) {
                if (field.getAnnotation(ParameterMapping.class) != null
                        || field.getAnnotation(PayloadMapping.class) != null
                        || !fieldNames.contains(field.getName())) {

                    fields.add(field);
                    fieldNames.add(field.getName());
                }
            }
            cls = cls.getSuperclass();
        }
        Collections.sort(fields, new ActionServlet.ParameterFieldComparator());

        fields.forEach(e -> {
            fieldMap.put(e.getName(), e);
        });
        try {
            action = actionType.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);

        }
    }

    public static ActionExecutor executor(Class<? extends Action> actionType) {
        return new ActionExecutor(actionType);
    }

    public ActionExecutor setProperty(String name, Object value) {
        if (!fieldMap.containsKey(name)) {
            throw new IllegalArgumentException("Field does not exist: " + name);
        }

        if (value != null) {
            Field field = fieldMap.get(name);
            field.setAccessible(true);
            try {
                field.set(action, ConvertUtils.convert(value,field.getType()));

            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }

        return this;
    }

    public Object execute() throws Exception {
        return action.execute();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Boolean.TYPE == boolean.class);


        ActionExecutor.executor(TestAction.class)
                .setProperty("message", "Hello Restruts!")
                .execute();
    }
}
