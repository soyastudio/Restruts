package soya.framework.action.dispatch;

import soya.framework.action.*;
import soya.framework.commons.util.URIUtils;

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
    private final String fragment;

    private ActionDispatch(ActionName actionName, Map<String, Assignment> assignments, String fragment) {
        this.actionName = actionName;
        this.assignments = assignments;

        List<String> params = new ArrayList<>();
        assignments.entrySet().forEach(e -> {
            if (e.getValue().getAssignmentType().equals(AssignmentType.PARAMETER)) {
                params.add(e.getValue().getExpression());
            }
        });
        parameterNames = params.toArray(new String[params.size()]);

        this.fragment = fragment.isEmpty() ? null : fragment;
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

    public String getFragment() {
        return fragment;
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

    public ActionResult dispatch(Object context) throws ActionDispatchException {
        return dispatch(context, AssignmentEvaluator.DEFAULT_EVALUATOR);
    }

    public ActionResult dispatch(Object context, AssignmentEvaluator evaluator) throws ActionDispatchException {
        ActionClass actionClass = ActionContext.getInstance().getActionMappings().actionClass(actionName);
        ActionCallable action = actionClass.newInstance();

        for (Field field : actionClass.getActionFields()) {
            Assignment assignment = getAssignment(field.getName());

            if (assignment == null) {
                assignment = new Assignment(AssignmentType.PARAMETER.toString(field.getName()));
            }

            Object value = evaluator.evaluate(assignment, context, field.getType());
            if (value != null) {
                field.setAccessible(true);
                try {
                    field.set(action, ConvertUtils.convert(value, field.getType()));
                } catch (IllegalAccessException e) {
                    throw new ActionDispatchException(e);
                }
            }
        }

        ActionResult actionResult = action.call();

        if (fragment != null) {
            actionResult = Fragment.process(actionResult, fragment);
        }

        return actionResult;
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
            URIUtils.splitQuery(uri.getQuery()).entrySet().forEach(e -> {
                String name = e.getKey();
                String expression = e.getValue().get(0);
                builder.addAssignment(name, expression);

            });
        }

        if (uri.getFragment() != null && !uri.getFragment().isEmpty()) {
            builder.addFragment(uri.getFragment());
        }

        return builder.create();

    }

    public static Builder builder(ActionName actionName) {
        return new Builder(actionName);
    }

    public static class Builder {
        private final ActionName actionName;
        private final Map<String, Assignment> params = new LinkedHashMap<>();
        private final StringBuilder fragmentBuilder = new StringBuilder();

        private Builder(ActionName actionName) {
            this.actionName = actionName;
        }

        public Builder addAssignment(String name, String assignment) {
            params.put(name, new Assignment(assignment));
            return this;
        }

        public Builder addAssignment(String name, AssignmentType assignmentType, String expression) {
            params.put(name, new Assignment(assignmentType, expression));
            return this;
        }

        public Builder addFragment(String fragment) {
            if (fragmentBuilder.length() == 0) {
                fragmentBuilder.append(fragment);
            } else {
                fragmentBuilder.append(".").append(fragment);
            }
            return this;
        }

        public ActionDispatch create() {
            return new ActionDispatch(actionName, params, fragmentBuilder.toString());
        }
    }
}
