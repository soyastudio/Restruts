package soya.framework.action;

public enum AssignmentMethod {

    VALUE("val"),
    PARAMETER("param"),
    ENVIRONMENT("env"),
    REFERENCE("ref");

    private final String function;

    AssignmentMethod(String function) {
        this.function = function;
    }

    public String toString(String expression) {
        return function + "(" + expression + ")";
    }

    public static AssignmentMethod getAssignmentMethod(String expression) {
        String token = expression.trim();
        if(token.indexOf('(') > 0 && token.endsWith(")")) {
            token = token.substring(0, token.indexOf('('));
        }

        switch (token) {
            case "val":
                return VALUE;
            case "env":
                return ENVIRONMENT;
            case "ref":
                return REFERENCE;

            default:
                throw new IllegalArgumentException("Cannot parse expression: " + expression);
        }
    }
}
