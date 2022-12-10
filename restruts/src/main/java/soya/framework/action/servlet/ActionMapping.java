package soya.framework.action.servlet;

import soya.framework.action.ActionName;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActionMapping implements Comparable<ActionMapping>, Serializable {
    private final ActionName actionName;
    private final String httpMethod;
    private final String path;
    private final String produce;

    private String description = "";
    private List<ParameterMapping> parameters = new ArrayList<>();

    private PathMapping pathMapping;

    ActionMapping(ActionName actionName, String httpMethod, String path, String produce) {
        this.actionName = actionName;
        this.httpMethod = httpMethod;
        this.path = path;
        this.produce = produce;

        this.pathMapping = new PathMapping(path);
    }

    public ActionName getActionName() {
        return actionName;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public String getProduce() {
        return produce;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ParameterMapping> getParameters() {
        return parameters;
    }

    public PathMapping getPathMapping() {
        return pathMapping;
    }

    public boolean match(HttpServletRequest request) {
        return httpMethod.equalsIgnoreCase(request.getMethod()) && pathMapping.match(request.getPathInfo());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionMapping)) return false;
        ActionMapping that = (ActionMapping) o;
        return actionName.equals(that.actionName) && httpMethod.equals(that.httpMethod) && path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionName, httpMethod, path);
    }

    @Override
    public int compareTo(ActionMapping o) {
        int result = this.path.compareTo(o.path);

        return result;
    }
}

