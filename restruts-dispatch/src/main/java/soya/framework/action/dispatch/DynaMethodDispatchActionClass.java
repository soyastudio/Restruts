package soya.framework.action.dispatch;

import org.apache.commons.beanutils.DynaProperty;
import soya.framework.action.ActionContext;
import soya.framework.action.ActionCreationException;
import soya.framework.action.ActionDescription;
import soya.framework.action.ActionName;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class DynaMethodDispatchActionClass extends DynaActionClassBase {

    private Method method;
    private Object service;

    public DynaMethodDispatchActionClass(Method method, ActionDescription actionDescription) {
        this.method = method;
        if (!Modifier.isStatic(method.getModifiers())) {
            service = ActionContext.getInstance().getService(method.getDeclaringClass());
        }

        String[] propNames = actionDescription.getActionPropertyNames();
        Class<?>[] propTypes = new Class<?>[propNames.length];
        DynaProperty[] properties = new DynaProperty[propNames.length];
        for (int i = 0; i < propNames.length; i++) {
            String propName = propNames[i];
            propTypes[i] = actionDescription.getActionPropertyDescription(propName).getPropertyType();
            properties[i] = new DynaProperty(propName, propTypes[i]);
        }

        init(actionDescription, properties);
    }

    public DynaMethodDispatchActionClass(Method method, ActionName actionName) {
        this.method = method;
        if (!Modifier.isStatic(method.getModifiers())) {
            service = ActionContext.getInstance().getService(method.getDeclaringClass());
        }

        ActionDescription.Builder builder = ActionDescription.builder()
                .actionName(actionName)
                .actionType(DynaMethodDispatchActionClass.class.getName());

        Parameter[] parameters = method.getParameters();
        DynaProperty[] properties = new DynaProperty[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            String propName = "arg" + i;
            properties[i] = new DynaProperty(propName, parameters[i].getType());

            // TODO: ActionPropertyDescription
        }

        init(builder.create(), properties);
    }

    @Override
    public DynaActionBean newInstance() throws ActionCreationException {
        return new DynaMethodDispatchActionBean(this);
    }

    static class DynaMethodDispatchActionBean extends DynaActionBeanBase<DynaMethodDispatchActionClass> {

        protected DynaMethodDispatchActionBean(DynaMethodDispatchActionClass dynaActionClass) throws ActionCreationException {
            super(dynaActionClass);
        }

        @Override
        protected Object execute() throws Exception {
            DynaMethodDispatchActionClass dynaClass = getDynaClass();
            DynaProperty[] properties = dynaClass.getDynaProperties();
            Object[] paramValues = new Object[properties.length];
            for (int i = 0; i < paramValues.length; i++) {
                paramValues[i] = get(properties[i].getName());
            }

            return dynaClass.method.invoke(dynaClass.service, paramValues);
        }
    }


}
