package soya.framework.action.mvc;


import java.io.Serializable;
import java.util.Objects;

public class MvcMapping implements Serializable {
    private final String method;
    private final String path;
    private final String to;
    private final Class<? extends MvcAction> actionType;


    MvcMapping(String method, String path, String to, Class<? extends MvcAction> actionType) {
        this.method = method;
        this.path = path;
        this.to = to;
        this.actionType = actionType;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getTo() {
        return to;
    }

    public Class<? extends MvcAction> getActionType() {
        return actionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MvcMapping)) return false;
        MvcMapping that = (MvcMapping) o;
        return method.equals(that.method) && path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }

    public static class Builder {

        private String method;
        private String path;
        private Class<? extends MvcAction> actionType;

        private Builder() {
        }


    }


}
