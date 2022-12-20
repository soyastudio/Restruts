package soya.framework.action;

import java.io.Serializable;

public final class ActionDescription implements Comparable<ActionDescription>, Serializable {

    private ActionName actionName;
    private String path;
    private String httpMethod;
    private String[] produces;
    private String displayName;
    private String[] description;
    private String externalLink;

    private String actionType;
    private String implementation;

    private ActionDescription(
            ActionName actionName,
            String path,
            String httpMethod,
            String[] produces,
            String displayName,
            String[] description,
            String externalLink,
            String actionType,
            String implementation
    ) {

        this.actionName = actionName;
        this.path = path;
        this.httpMethod = httpMethod;
        this.produces = produces;
        this.displayName = displayName;
        this.description = description;
        this.externalLink = externalLink;
        this.actionType = actionType;
        this.implementation = implementation;
    }

    public ActionName getActionName() {
        return actionName;
    }

    public String getPath() {
        return path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String[] getProduces() {
        return produces;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String[] getDescription() {
        return description;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public String getActionType() {
        return actionType;
    }

    public String getImplementation() {
        return implementation;
    }

    @Override
    public int compareTo(ActionDescription o) {
        return actionName.compareTo(o.actionName);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ActionName actionName;
        private String path;
        private String httpMethod;
        private String[] produces;
        private String displayName;
        private String[] description;
        private String externalLink;

        private String actionType;
        private String implementation;

        private Builder() {
        }

        public Builder fromActionClass(Class<? extends ActionCallable> cls) {
            ActionDefinition definition = cls.getAnnotation(ActionDefinition.class);
            this.actionName = ActionName.create(definition.domain(), definition.name());
            this.path = definition.path();
            this.httpMethod = definition.method().name();
            this.produces = definition.produces();
            this.displayName = definition.displayName();
            this.description = definition.description();
            this.externalLink = definition.externalLink();

            this.actionType = "ACTION_CLASS";
            this.implementation = "class://" + cls.getName();
            return this;
        }

        public ActionDescription create() {
            return new ActionDescription(actionName, path, httpMethod, produces, displayName, description, externalLink, actionType, implementation);
        }
    }
}
