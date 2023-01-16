package soya.framework.pattern;

import java.util.ArrayList;
import java.util.List;

public class FunctionExpression {
    private final String name;
    private final String[] parameters;

    private FunctionExpression(String name, String[] parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public String[] getParameters() {
        return parameters;
    }

    public static FunctionExpression parse(String expression) {
        String name = "";
        List<String> paramList = new ArrayList<>();
        return new FunctionExpression(name, paramList.toArray(new String[paramList.size()]));
    }
}
