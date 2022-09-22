package soya.framework.action;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public final class ActionClass implements Serializable {

    private final transient Class<? extends ActionCallable> actionType;
    private transient Map<String, Field> actionFields = new LinkedHashMap<>();
    private transient Map<String, Field> options = new LinkedHashMap<>();

    private final ActionName actionName;
    private final String produce;

    private ActionClass(Class<? extends ActionCallable> actionType) {

        ActionDefinition mapping = actionType.getAnnotation(ActionDefinition.class);
        if (mapping == null) {
            throw new IllegalArgumentException("Class is not annotated as 'OperationMapping': " + actionType.getName());
        }

        this.actionType = actionType;
        for (Field field : findActionFields()) {
            ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);

            actionFields.put(field.getName(), field);
            if(!actionProperty.option().isEmpty()) {
                options.put(actionProperty.option(), field);
            }
        }

        this.actionName = ActionName.create(mapping.domain(), mapping.name());
        this.produce = mapping.produces()[0];

    }

    public ActionName getActionName() {
        return actionName;
    }

    public Class<? extends ActionCallable> getActionType() {
        return actionType;
    }

    public Field[] getActionFields() {
        return actionFields.values().toArray(new Field[actionFields.size()]);
    }

    public Field getActionField(String name) {
        if(name.length() == 1) {
            return options.get(name);
        } else {
            return actionFields.get(name);
        }
    }

    public String getProduce() {
        return produce;
    }

    public String toURI() {
        StringBuilder builder = new StringBuilder(actionName.toString());
        if (actionFields.size() > 0) {
            builder.append("?");
            actionFields.entrySet().forEach(e -> {
                builder.append(e.getKey()).append("=assign(").append(e.getKey()).append(")").append("&");
            });

            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public ActionCallable newInstance() throws ActionCreationException {
        try {
            ActionCallable action = actionType.newInstance();
            actionFields.values().forEach(field -> {
                ActionProperty property = field.getAnnotation(ActionProperty.class);
                if (property.required() && !property.defaultValue().isEmpty()) {
                    Object value = ConvertUtils.convert(property.defaultValue(), field.getType());

                    if (value != null) {
                        field.setAccessible(true);
                        try {
                            field.set(action, value);
                        } catch (IllegalAccessException e) {
                            throw new ActionCreationException(e);
                        }
                    }
                }
            });

            return action;
        } catch (Exception e) {
            throw new ActionCreationException(e);
        }
    }

    private Field[] findActionFields() {
        List<Field> fields = new ArrayList<>();
        Set<String> fieldNames = new HashSet<>();
        Class<?> cls = actionType;
        while (!cls.getName().equals("java.lang.Object")) {

            for (Field field : cls.getDeclaredFields()) {
                if (field.getAnnotation(ActionProperty.class) != null
                        && !fieldNames.contains(field.getName())) {

                    fields.add(field);
                    fieldNames.add(field.getName());
                }
            }

            cls = cls.getSuperclass();
        }

        Collections.sort(fields, new ParameterFieldComparator());

        return fields.toArray(new Field[fields.size()]);
    }

    public static ActionClass get(Class<? extends ActionCallable> actionType) {
        return new ActionClass(actionType);
    }

    private final class ParameterFieldComparator implements Comparator<Field> {

        @Override
        public int compare(Field o1, Field o2) {
            if (o1.getAnnotation(ActionProperty.class) != null && o2.getAnnotation(ActionProperty.class) != null) {
                ActionProperty a1 = o1.getAnnotation(ActionProperty.class);
                ActionProperty a2 = o2.getAnnotation(ActionProperty.class);

                int result = ActionProperty.PropertyType.index(a1.parameterType())
                        - ActionProperty.PropertyType.index(a2.parameterType());
                if (result != 0) {
                    return result;
                }

                result = a1.displayOrder() - a2.displayOrder();
                if (result != 0) {
                    return result;
                }
            }

            return o1.getName().compareTo(o2.getName());
        }
    }

}
