package soya.framework.action.dispatch;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import soya.framework.action.*;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.*;

public class DynaActionDispatchActionClass extends DynaActionClassBase {

    private static Map<ActionName, DynaActionDispatchActionClass> DYNA_ACTION_CLASSES = new HashMap<>();

    private final ActionDispatch actionDispatch;

    private Map<String, Field> parameterFields = new HashMap<>();

    public DynaActionDispatchActionClass(ActionName actionName, String dispatch) {
        this.actionDispatch = ActionDispatch.fromURI(URI.create(dispatch));

        ActionClass actionClass = ActionClass.get(actionDispatch.getActionName());
        ActionDescription.Builder builder = ActionDescription.builder()
                .actionName(actionName)
                .actionType(DynaActionDispatchActionClass.class.getName());

        int len = actionDispatch.getParameterNames().length;
        DynaProperty[] properties = new DynaProperty[len];
        int index = 0;
        for (Field field : actionClass.getActionFields()) {
            Assignment assignment = actionDispatch.getAssignment(field.getName());
            if (assignment != null && assignment.getAssignmentType().equals(AssignmentType.PARAMETER)) {
                properties[index] = new DynaProperty(assignment.getExpression(), field.getType());
                parameterFields.put(properties[index].getName(), field);
                builder.addProperty(ActionPropertyDescription.create(field.getName(), field.getType(), field.getAnnotation(ActionProperty.class)));

                index++;
            }
        }

        init(builder.create(), properties);

        DYNA_ACTION_CLASSES.put(actionName, this);
    }

    public ActionProperty getActionProperty(String name) {
        if (!parameterFields.containsKey(name)) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        return parameterFields.get(name).getAnnotation(ActionProperty.class);
    }

    @Override
    public DynaActionBean newInstance() throws ActionCreationException {
        return new DynaDispatchActionBean(this, actionDispatch);
    }

    public static ActionName[] actions() {
        List<ActionName> list = new ArrayList<>(DYNA_ACTION_CLASSES.keySet());
        Collections.sort(list);
        return list.toArray(new ActionName[list.size()]);
    }

    public static DynaActionDispatchActionClass get(ActionName actionName) {
        return DYNA_ACTION_CLASSES.get(actionName);
    }

    static class DynaDispatchActionBean extends DynaActionBeanBase<DynaActionDispatchActionClass> {
        private ActionDispatch actionDispatch;

        protected DynaDispatchActionBean(DynaActionDispatchActionClass dynaClass, ActionDispatch actionDispatch) throws ActionCreationException {
            super(dynaClass);
            this.actionDispatch = actionDispatch;
        }

        @Override
        public Object execute() throws Exception {
            return actionDispatch.dispatch(bean, new AssignmentEvaluator() {
                @Override
                protected Object fromParameter(String exp, Object context, Class<?> type) {
                    DynaBean dynaBean = (DynaBean) context;
                    return ConvertUtils.convert(dynaBean.get(exp), type);
                }

                @Override
                protected Object fromReference(String exp, Object context, Class<?> type) {
                    return ActionContext.getInstance().getService(type);
                }

            }).get();
        }
    }
}
