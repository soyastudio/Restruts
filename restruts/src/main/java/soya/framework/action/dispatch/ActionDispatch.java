package soya.framework.action.dispatch;

import soya.framework.action.*;
import soya.framework.common.util.StringUtils;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ActionDispatch {
    private static final Evaluator DEFAULT_EVALUATOR = new DefaultEvaluator();

    private final ActionName actionName;
    private final Map<String, Assignment> assignments;
    private final String[] parameterNames;

    private ActionDispatch(ActionName actionName, Map<String, Assignment> assignments) {
        this.actionName = actionName;
        this.assignments = assignments;

        List<String> params = new ArrayList<>();
        assignments.entrySet().forEach(e -> {
            if (e.getValue().getAssignmentMethod().equals(AssignmentMethod.PARAMETER)) {
                params.add(e.getValue().getExpression());
            }
        });
        parameterNames = params.toArray(new String[params.size()]);
    }

    public ActionName getActionName() {
        return actionName;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

    public Assignment getAssignment(String propName) {
        return assignments.get(propName);
    }

    public ActionCallable create(Object context) {
        return create(context, DEFAULT_EVALUATOR);
    }

    public ActionCallable create(Object context, Evaluator evaluator) {
        Class<? extends ActionCallable> actionType = null;
        if ("class".equals(actionName.getDomain())) {
            try {
                actionType = (Class<? extends ActionCallable>) Class.forName(actionName.getName());

            } catch (ClassNotFoundException e) {
                throw new ActionDispatchException(e);
            }
        } else {
            actionType = ActionContext.getInstance().getActionMappings().actionClass(actionName).getActionType();
        }

        try {
            ActionCallable action = actionType.newInstance();
            ActionClass actionClass = ActionClass.get(actionType);
            Field[] fields = actionClass.getActionFields();
            for (Field field : fields) {
                if (assignments.containsKey(field.getName())) {
                    Assignment assignment = assignments.get(field.getName());
                    Object value = evaluator.evaluate(assignment, context, field.getType());
                    if (value != null) {
                        field.setAccessible(true);
                        field.set(action, value);
                    }
                }
            }

            return action;

        } catch (InstantiationException | IllegalAccessException e) {
            throw new ActionDispatchException(e);
        }

    }

    public String toURI() {
        StringBuilder builder = new StringBuilder(actionName.toString());
        if (assignments.size() > 0) {
            builder.append("?");
            assignments.entrySet().forEach(e -> {
                builder.append(e.getKey()).append("=").append(e.getValue().toString()).append("&");
            });
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static ActionDispatch fromAnnotation(ActionDispatchPattern pattern) {
        ActionDispatch.Builder builder = builder(ActionName.fromURI(URI.create(pattern.uri())));
        for (ActionPropertyAssignment assignment : pattern.propertyAssignments()) {
            builder.addAssignment(assignment.name(), assignment.assignmentMethod(), assignment.expression());
        }

        return builder.create();
    }

    public static ActionDispatch fromURI(String uri) {
        return fromURI(URI.create(uri));
    }

    public static ActionDispatch fromURI(URI uri) {
        Builder builder;
        if ("class".equals(uri.getScheme())) {
            try {
                Class<? extends ActionCallable> cls = (Class<? extends ActionCallable>) Class.forName(uri.getHost());
                ActionDefinition definition = cls.getAnnotation(ActionDefinition.class);
                builder = builder(ActionName.create(definition.domain(), definition.name()));

            } catch (Exception e) {
                throw new IllegalArgumentException("Cannot create action dispatch from uri: " + uri.toString());
            }

        } else {
            builder = builder(ActionName.create(uri.getScheme(), uri.getHost()));
        }

        if (uri.getQuery() != null) {
            StringUtils.splitQuery(uri.getQuery()).entrySet().forEach(e -> {
                String name = e.getKey();
                String expression = e.getValue().get(0);
                builder.addAssignment(name, expression);

            });
        }

        return builder.create();

    }

    public static Builder builder(ActionName actionName) {
        return new Builder(actionName);
    }

    public static class Builder {
        private final ActionName actionName;
        private final Map<String, Assignment> params = new LinkedHashMap<>();

        private Builder(ActionName actionName) {
            this.actionName = actionName;
        }

        public Builder addAssignment(String name, String assignment) {
            params.put(name, new Assignment(assignment));
            return this;
        }

        public Builder addAssignment(String name, AssignmentMethod assignmentMethod, String expression) {
            params.put(name, new Assignment(assignmentMethod, expression));
            return this;
        }

        public ActionDispatch create() {
            return new ActionDispatch(actionName, params);
        }
    }
}
