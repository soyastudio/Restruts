package soya.framework.action.actions.reflect;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

@ActionDefinition(domain = "reflect",
        name = "bean-info",
        path = "/bean-info",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "About",
        description = "Print as markdown format.")
public class BeanInfoAction extends Action<String> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true, option = "t")
    private String type;

    @Override
    public String execute() throws Exception {

        Class<?> clazz = Class.forName(type);
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        beanInfo.getBeanDescriptor();

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for(PropertyDescriptor prop: propertyDescriptors) {
            if(prop.getWriteMethod() != null) {
                System.out.println("================= " + prop.getName() + ": " + prop.getPropertyType().getName());
            }
        }

        return "TODO";
    }
}
