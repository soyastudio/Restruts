package soya.framework.action.dispatch;

public enum AssignmentMethod {
    VALUE("val"),
    PARAMETER("param"),
    RESOURCE("res"),
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
            case "param":
                return PARAMETER;
            case "res":
                return RESOURCE;
            case "ref":
                return REFERENCE;

            default:
                throw new IllegalArgumentException("Cannot parse expression: " + expression);
        }
    }
}
