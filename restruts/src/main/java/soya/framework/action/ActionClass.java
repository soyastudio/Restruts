package soya.framework.action;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class ActionClass implements Serializable {

    private static Map<Class<? extends ActionCallable>, ActionClass> ACTION_CLASSES = new ConcurrentHashMap<>();
    private static Map<ActionName, AtomicLong> COUNTS = new ConcurrentHashMap<>();
    private static final AtomicLong TOTAL_COUNT = new AtomicLong();

    private final transient Class<? extends ActionCallable> actionType;
    private transient Map<String, Field> actionFields = new LinkedHashMap<>();
    private transient Map<String, Field> options = new LinkedHashMap<>();

    private final ActionName actionName;
    private final String produce;

    private ActionClass(Class<? extends ActionCallable> actionType) {

        ActionDefinition mapping = actionType.getAnnotation(ActionDefinition.class);
        Objects.requireNonNull(mapping, "Class is not annotated as 'OperationMapping': " + actionType.getName());

        ActionName actionName = ActionName.create(mapping.domain(), mapping.name());
        if (COUNTS.containsKey(actionName)) {
            throw new IllegalArgumentException("Action name '" + actionName + "' already exists.");
        }

        this.actionName = actionName;
        this.actionType = actionType;
        for (Field field : findActionFields()) {
            ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);

            actionFields.put(field.getName(), field);
            if (!actionProperty.option().isEmpty()) {
                options.put(actionProperty.option(), field);
            }
        }
        this.produce = mapping.produces()[0];

        ACTION_CLASSES.put(actionType, this);
        COUNTS.put(actionName, new AtomicLong());

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
        if (name.length() == 1) {
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

    ActionResult createResult(ActionCallable action, Object result) {
        ActionClass actionClass = ACTION_CLASSES.get(action.getClass());

        Map<String, Object> params = new LinkedHashMap<>();
        for (Field field : actionClass.getActionFields()) {
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(action);

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (fieldValue != null) {
                params.put(field.getName(), fieldValue);
            }
        }

        ActionResult actionResult = new DefaultActionResult(actionClass.getActionName(), COUNTS.get(actionName).getAndIncrement(), params, result);
        TOTAL_COUNT.getAndIncrement();

        return actionResult;
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
        if (!ACTION_CLASSES.containsKey(actionType)) {
            new ActionClass(actionType);
        }

        return ACTION_CLASSES.get(actionType);
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

    private static final class DefaultActionResult implements ActionResult {

        private final ActionName actionName;
        private final long timestamp;
        private final long sequence;

        private final Map<String, Object> parameters;
        private final Object value;

        private DefaultActionResult(ActionName actionName, long sequence, Map<String, Object> parameters, Object value) {
            this.timestamp = System.currentTimeMillis();
            this.actionName = actionName;
            this.sequence = sequence;
            this.parameters = parameters;
            this.value = value;

        }

        public ActionName actionName() {
            return actionName;
        }

        public Object get() {
            return value;
        }

        public boolean success() {
            return !(value instanceof Throwable);
        }

        public boolean empty() {
            return value == null;
        }
    }

}
