package soya.framework.action.dispatch;

import org.reflections.Reflections;
import soya.framework.action.ActionContext;
import soya.framework.action.ActionName;
import soya.framework.action.ActionResult;
import soya.framework.commons.util.URIUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Fragment {

    private static Fragment me;

    private Map<String, Class<? extends FragmentProcessor>> functions = new ConcurrentHashMap<>();

    protected Fragment() {
        if (me == null) {
            scan("soya.framework");
            me = this;
        } else {
            throw new IllegalStateException("Fragment instance is already created.");
        }
    }

    protected Map<String, Class<? extends FragmentProcessor>> getFunctions() {
        return new HashMap<>(functions);
    }

    protected void scan(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(FragmentFunction.class);
        set.forEach(c -> {
            FragmentFunction function = c.getAnnotation(FragmentFunction.class);
            functions.put(function.value(), (Class<? extends FragmentProcessor>) c);
        });
    }

    public static ActionResult process(ActionResult result, String fragment) {
        ActionResultExchange exchange = new ActionResultExchange(result);
        parse(fragment).forEach(e -> {
            exchange.set(e.process(exchange));
        });

        return exchange;
    }

    protected static Fragment getInstance() {
        if (me == null) {
            new Fragment();
        }

        return me;
    }

    private static List<FragmentProcessor> parse(String chainExpr) {
        List<FragmentProcessor> chain = new ArrayList<>();
        StringBuilder builder = null;
        char[] arr = chainExpr.toCharArray();

        int depth = 0;
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if (builder == null) {
                builder = new StringBuilder();
            }
            builder.append(c);

            if (c == '(') {
                depth++;

            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    if (builder.charAt(0) == '.') {
                        builder.deleteCharAt(0);
                    }

                    chain.add(function(builder.toString()));
                    builder = null;
                }
            }
        }

        return chain;
    }

    private static FragmentProcessor function(String function) {
        try {
            String name = function.substring(0, function.indexOf("("));
            String expr = function.substring(function.indexOf("(") + 1, function.lastIndexOf(")"));

            String[] args = expr == null || expr.trim().length() == 0 ? new String[0]
                    : URIUtils.trim(expr.split(","));
            Class<? extends FragmentProcessor> cls = getInstance().functions.get(name);

            return cls.getConstructor(new Class[]{String[].class}).newInstance(new Object[]{args});

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Cannot parse fragment function from '" + function + "'", e);
        }


    }

    static class ActionResultExchange implements ActionResult {

        private final ActionResult actionResult;

        private Object value;

        public ActionResultExchange(ActionResult actionResult) {
            this.actionResult = actionResult;
            this.value = actionResult.get();
        }

        @Override
        public ActionName actionName() {
            return actionResult.actionName();
        }

        @Override
        public Object get() {
            return value;
        }

        public void set(Object value) {
            this.value = value;
        }

        @Override
        public boolean success() {
            return true;
        }
    }

    public static void main(String[] args) {
        getInstance();
    }
}
