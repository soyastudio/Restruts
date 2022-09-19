package soya.framework.action.dispatch;

import soya.framework.action.*;
import soya.framework.common.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.*;

public final class ActionDispatch {

    private final ActionName actionName;
    private final Map<String, Assignment> assignments;

    private ActionDispatch(ActionName actionName, Map<String, Assignment> assignments) {
        this.actionName = actionName;
        this.assignments = assignments;

    }

    public <T> ActionCallable create(T context, Evaluator<T> evaluator) {

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
                    AssignmentMethod assignmentMethod = assignment.getAssignmentMethod();
                    String expression = assignment.getExpression();

                    Object value = null;
                    if (AssignmentMethod.VALUE.equals(assignmentMethod)) {
                        value = expression;

                    } else if (AssignmentMethod.RESOURCE.equals(assignmentMethod)) {
                        value = Resources.getResourceAsString(expression);

                    } else if (AssignmentMethod.REFERENCE.equals(assignmentMethod)) {
                        value = ConvertUtils.convert(evaluator.evaluate(expression, context), field.getType());
                    }

                    if (value != null) {
                        field.setAccessible(true);
                        field.set(action, ConvertUtils.convert(value, field.getType()));
                    }
                }
            }

            return action;

        } catch (InstantiationException | IllegalAccessException | IOException e) {
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

    public static ActionDispatch fromAction(ActionClass actionClass) {
        ActionDispatch.Builder builder = ActionDispatch.builder(actionClass.getActionName());
        for (Field field : actionClass.getActionFields()) {
            builder.addAssignment(field.getName(), AssignmentMethod.PARAMETER, field.getName());
        }

        return builder.create();
    }

    public static ActionDispatch fromURI(String uri) {
        return fromURI(URI.create(uri));
    }

    public static ActionDispatch fromURI(URI uri) {
        Builder builder = builder(ActionName.create(uri.getScheme(), uri.getHost()));
        if (uri.getQuery() != null) {
            StringUtils.splitQuery(uri.getQuery()).entrySet().forEach(e -> {
                String name = e.getKey();
                String expression = e.getValue().get(0);
                builder.addAssignment(name, expression);

            });
        }

        return builder.create();

    }

    public static ActionDispatch fromAnnotation(ActionDispatchPattern pattern) {
        ActionDispatch.Builder builder = builder(ActionName.fromURI(URI.create(pattern.uri())));
        for(ActionPropertyAssignment assignment: pattern.propertyAssignments()) {
            builder.addAssignment(assignment.name(), assignment.assignmentMethod(), assignment.expression());
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
