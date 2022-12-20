package soya.framework.action;

import java.io.Serializable;
import java.lang.reflect.Field;

public final class ActionPropertyDescription implements Comparable<ActionPropertyDescription>, Serializable {

    private String name;
    private String option;
    private String[] description;
    private boolean required;
    private int displayOrder = 5;
    private ActionParameterType parameterType;
    private String contentType = MediaType.TEXT_PLAIN;
    private Class<?> propertyType;

    public static ActionPropertyDescription fromActionField(Field field) {
        ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
        ActionPropertyDescription propDesc = new ActionPropertyDescription();

        propDesc.name = field.getName();
        propDesc.propertyType = field.getType();

        propDesc.option = actionProperty.option();
        propDesc.description = actionProperty.description();
        propDesc.required = actionProperty.required();
        propDesc.displayOrder = actionProperty.displayOrder();
        propDesc.parameterType = actionProperty.parameterType();
        propDesc.contentType = actionProperty.contentType();

        return propDesc;
    }

    public String getName() {
        return name;
    }

    public String getOption() {
        return option;
    }

    public String[] getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public ActionParameterType getParameterType() {
        return parameterType;
    }

    public String getContentType() {
        return contentType;
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    @Override
    public int compareTo(ActionPropertyDescription o) {
        int result = ActionParameterType.index(parameterType) - ActionParameterType.index(o.parameterType);
        if(result == 0) {
            result = displayOrder - o.displayOrder;
        }

        if(result == 0) {
            result = name.compareTo(o.name);
        }

        return result;
    }
}
