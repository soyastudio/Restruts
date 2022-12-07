package soya.framework.action.dispatch;

import soya.framework.action.ActionClass;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionName;

public final class DynaActionDefinition {
    private final ActionName actionName;

    private String path;
    private ActionDefinition.HttpMethod method;
    private String[] produces;
    private String displayName;
    private String[] description = {};
    private String externalLink = "";

    public DynaActionDefinition(ActionName actionName, ActionClass actionClass) {
        this.actionName = actionName;
        ActionDefinition definition = actionClass.getActionType().getAnnotation(ActionDefinition.class);

        this.method = definition.method();

    }

    public DynaActionDefinition(ActionName actionName, DynaActionClass actionClass) {
        this.actionName = actionName;
        DynaActionDefinition definition = actionClass.getActionDefinition();

        this.method = definition.method;

    }

    public ActionName getActionName() {
        return actionName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ActionDefinition.HttpMethod getMethod() {
        return method;
    }

    public void setMethod(ActionDefinition.HttpMethod method) {
        this.method = method;
    }

    public String[] getProduces() {
        return produces;
    }

    public void setProduces(String[] produces) {
        this.produces = produces;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }
}
