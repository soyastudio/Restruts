package soya.framework.action;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private ActionPropertyDescription[] properties;

    private ActionDescription(
            ActionName actionName,
            String path,
            String httpMethod,
            String[] produces,
            String displayName,
            String[] description,
            String externalLink,
            String actionType,
            String implementation,
            ActionPropertyDescription[] properties
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
        this.properties = properties;
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

    public ActionPropertyDescription[] getProperties() {
        return properties;
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

        private List<ActionPropertyDescription> properties = new ArrayList<>();

        private Builder() {
        }

        public Builder fromActionClass(ActionClass actionClass) {
            Class<? extends ActionCallable> cls = actionClass.getActionType();
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

            for (Field field : actionClass.getActionFields()) {
                properties.add(ActionPropertyDescription.fromActionField(field));
            }

            return this;
        }

        public ActionDescription create() {
            Collections.sort(properties);
            return new ActionDescription(
                    actionName,
                    path,
                    httpMethod,
                    produces,
                    displayName,
                    description,
                    externalLink,
                    actionType,
                    implementation,
                    properties.toArray(new ActionPropertyDescription[properties.size()])
            );
        }
    }
}
