package soya.framework.action.dispatch;

import soya.framework.action.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

public class ActionDispatch {
    private final ActionName actionName;
    private final Map<String, ParameterAssignment> assignments;

    private ActionDispatch(ActionName actionName, Map<String, ParameterAssignment> assignments) {
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
                    ParameterAssignment assignment = assignments.get(field.getName());
                    AssignmentMethod assignmentMethod = assignment.getAssignmentMethod();
                    String expression = assignment.getExpression();

                    Object value = null;
                    if (AssignmentMethod.VALUE.equals(assignmentMethod)) {
                        value = expression;

                    } else if (AssignmentMethod.ENVIRONMENT.equals(assignmentMethod)) {
                        value = ActionContext.getInstance().getProperty(expression);

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
            splitQuery(uri.getQuery()).entrySet().forEach(e -> {
                String name = e.getKey();
                String expression = e.getValue().get(0);
                builder.addAssignment(name, expression);

            });
        }

        return builder.create();

    }

    public static Map<String, List<String>> splitQuery(String query) {
        Map<String, List<String>> params = new HashMap<>();
        try {
            params = splitQuery(query, "UTF-8");

        } catch (UnsupportedEncodingException e) {

        }
        return params;
    }

    public static Map<String, List<String>> splitQuery(String query, String encoding) throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
        if (query != null && !query.isEmpty()) {
            final String[] pairs = query.split("&");
            for (String pair : pairs) {
                final int idx = pair.indexOf("=");
                final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), encoding) : pair;
                if (!query_pairs.containsKey(key)) {
                    query_pairs.put(key, new LinkedList<String>());
                }
                final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
                query_pairs.get(key).add(value);
            }
        }
        return query_pairs;
    }

    public static Builder builder(ActionName actionName) {
        return new Builder(actionName);
    }

    static class ParameterAssignment {
        private final AssignmentMethod assignmentMethod;
        private final String expression;

        ParameterAssignment(String assignment) {
            this.assignmentMethod = AssignmentMethod.getAssignmentMethod(assignment);
            this.expression = assignment.substring(assignment.indexOf('(') + 1, assignment.lastIndexOf(')')).trim();
        }

        ParameterAssignment(AssignmentMethod assignmentMethod, String expression) {
            this.assignmentMethod = assignmentMethod;
            this.expression = expression;
        }

        public AssignmentMethod getAssignmentMethod() {
            return assignmentMethod;
        }

        public String getExpression() {
            return expression;
        }

        @Override
        public String toString() {
            return assignmentMethod.toString(expression);
        }
    }

    public static class Builder {
        private final ActionName actionName;
        private final Map<String, ParameterAssignment> params = new LinkedHashMap<>();

        private Builder(ActionName actionName) {
            this.actionName = actionName;
        }

        public Builder addAssignment(String name, String assignment) {
            params.put(name, new ParameterAssignment(assignment));
            return this;
        }

        public Builder addAssignment(String name, AssignmentMethod assignmentMethod, String expression) {
            params.put(name, new ParameterAssignment(assignmentMethod, expression));
            return this;
        }

        public ActionDispatch create() {
            return new ActionDispatch(actionName, params);
        }
    }
}
