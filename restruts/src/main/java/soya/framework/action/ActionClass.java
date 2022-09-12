package soya.framework.action;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public final class ActionClass implements Serializable {
    private final transient Class<? extends ActionCallable> actionType;
    private final transient Field[] actionFields;

    private final ActionName actionName;
    private final ActionProperty[] actionProperties;

    private ActionClass(Class<? extends ActionCallable> actionType) {

        OperationMapping mapping = actionType.getAnnotation(OperationMapping.class);
        if (mapping == null) {
            throw new IllegalArgumentException("Class is not annotated as 'OperationMapping': " + actionType.getName());
        }

        this.actionType = actionType;
        this.actionFields = findActionFields();

        this.actionName = ActionName.create(mapping.domain(), mapping.name());
        this.actionProperties = new ActionProperty[actionFields.length];
        for (int i = 0; i < actionFields.length; i++) {
            actionProperties[i] = new ActionProperty(actionFields[i]);
        }
    }

    public ActionName getActionName() {
        return actionName;
    }

    private Field[] findActionFields() {
        List<Field> fields = new ArrayList<>();
        Set<String> fieldNames = new HashSet<>();
        Class<?> cls = actionType;
        while (!cls.getName().equals("java.lang.Object")) {

            for (Field field : cls.getDeclaredFields()) {
                if ((field.getAnnotation(ParameterMapping.class) != null
                        || field.getAnnotation(PayloadMapping.class) != null)
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

    public Class<? extends ActionCallable> getActionType() {
        return actionType;
    }

    public Field[] getActionFields() {
        return actionFields;
    }

    public ActionSignature signature() {
        ActionSignature.Builder builder = ActionSignature.builder(actionName);
        for (ActionProperty prop : actionProperties) {
            builder.addAssignment(prop.getName(), ActionSignature.ParameterAssignment.PARAM);
        }

        return builder.create();
    }

    public static ActionClass get(Class<? extends ActionCallable> actionType) {
        return new ActionClass(actionType);
    }

    final class ParameterFieldComparator implements Comparator<Field> {

        @Override
        public int compare(Field o1, Field o2) {
            if (o1.getAnnotation(PayloadMapping.class) != null) {
                return 1;
            } else if (o2.getAnnotation(PayloadMapping.class) != null) {
                return -1;
            }

            if (o1.getAnnotation(ParameterMapping.class) != null && o2.getAnnotation(ParameterMapping.class) != null) {
                int result = ParameterMapping.ParameterType.index(o1.getAnnotation(ParameterMapping.class).parameterType())
                        - ParameterMapping.ParameterType.index(o2.getAnnotation(ParameterMapping.class).parameterType());
                if (result != 0) {
                    return result;
                }
            }

            return o1.getName().compareTo(o2.getName());
        }
    }

}
