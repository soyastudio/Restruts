package soya.framework.action;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

public class ActionSignature {
    private final ActionName actionName;
    private final Map<String, ParameterAssignment> assignments;

    private ActionSignature(ActionName actionName, Map<String, ParameterAssignment> assignments) {
        this.actionName = actionName;
        this.assignments = assignments;

    }

    public <T> ActionCallable create(T context, Evaluator<T> evaluator) {

        Class<? extends ActionCallable> actionType = null;
        if("class".equals(actionName.getDomain())) {
            try {
                actionType = (Class<? extends ActionCallable>) Class.forName(actionName.getName());
            } catch (ClassNotFoundException e) {
                throw new ActionSignatureException(e);
            }
        } else {
            actionType = ActionContext.getInstance().getActionMappings().actionClass(actionName).getActionType();
        }

        System.out.println("------------------- " + actionType.getName());

        try {
            ActionCallable action = actionType.newInstance();

            return action;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ActionSignatureException(e);
        }

    }

    public String toURI() {
        StringBuilder builder = new StringBuilder(actionName.toString());
        if (assignments.size() > 0) {
            builder.append("?");
            assignments.entrySet().forEach(e -> {
                builder.append(e.getKey()).append("=").append(e.getValue().toString(e.getKey())).append("&");
            });
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static ActionSignature fromURI(String uri) {
        return fromURI(URI.create(uri));
    }

    public static ActionSignature fromURI(URI uri) {
        Builder builder = builder(ActionName.create(uri.getScheme(), uri.getHost()));
        if (uri.getQuery() != null) {
            splitQuery(uri.getQuery()).entrySet().forEach(e -> {
                String name = e.getKey();
                String value = e.getValue().get(0);


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

    public enum ParameterAssignment {
        VALUE("val"),
        PARAM("param"),
        REFERENCE("ref");

        private final String function;

        ParameterAssignment(String function) {
            this.function = function;
        }

        public String toString(String var) {
            return function + "(" + var + ")";
        }
    }

    public static class Builder {
        private final ActionName actionName;
        private final Map<String, ParameterAssignment> params = new LinkedHashMap<>();

        private Builder(ActionName actionName) {
            this.actionName = actionName;
        }

        public Builder addAssignment(String name, ParameterAssignment parameterAssignment) {
            params.put(name, parameterAssignment);
            return this;
        }

        public ActionSignature create() {
            return new ActionSignature(actionName, params);
        }
    }
}
