package soya.framework.action;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public final class ActionClass implements Serializable {

    private final transient Class<? extends ActionCallable> actionType;
    private final transient Field[] actionFields;

    private final ActionName actionName;
    private final String produce;

    private ActionClass(Class<? extends ActionCallable> actionType) {

        ActionDefinition mapping = actionType.getAnnotation(ActionDefinition.class);
        if (mapping == null) {
            throw new IllegalArgumentException("Class is not annotated as 'OperationMapping': " + actionType.getName());
        }

        this.actionType = actionType;
        this.actionFields = findActionFields();

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
        return actionFields;
    }

    public String getProduce() {
        return produce;
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
                int result = ActionProperty.PropertyType.index(o1.getAnnotation(ActionProperty.class).parameterType())
                        - ActionProperty.PropertyType.index(o2.getAnnotation(ActionProperty.class).parameterType());
                if (result != 0) {
                    return result;
                }
            }

            return o1.getName().compareTo(o2.getName());
        }
    }

}
