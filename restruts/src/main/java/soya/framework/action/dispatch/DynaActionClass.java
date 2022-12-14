package soya.framework.action.dispatch;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import soya.framework.action.*;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.*;

public class DynaActionClass implements DynaClass {

    private static Map<ActionName, DynaActionClass> DYNA_ACTION_CLASSES = new HashMap<>();

    private final DynaActionDefinition actionDefinition;
    private final ActionDispatch actionDispatch;
    private final BasicDynaClass dynaClass;

    private Map<String, Field> parameterFields = new HashMap<>();

    public DynaActionClass(ActionName actionName, String dispatch) {
        this(actionName, dispatch, false);
    }

    protected DynaActionClass(ActionName actionName, String dispatch, boolean singleton) {
        if (singleton && DYNA_ACTION_CLASSES.containsKey(actionName)) {
            throw new IllegalArgumentException("Action class already exist: " + actionName.toString());
        }

        this.actionDispatch = ActionDispatch.fromURI(URI.create(dispatch));

        ActionClass actionClass = ActionClass.get(actionDispatch.getActionName());
        this.actionDefinition = new DynaActionDefinition(actionName, actionClass);
        int len = actionDispatch.getParameterNames().length;

        DynaProperty[] properties = new DynaProperty[len];
        int index = 0;
        for (Field field : actionClass.getActionFields()) {
            Assignment assignment = actionDispatch.getAssignment(field.getName());
            if (assignment != null && assignment.getAssignmentType().equals(AssignmentType.PARAMETER)) {
                properties[index] = new DynaProperty(assignment.getExpression(), field.getType());
                parameterFields.put(properties[index].getName(), field);
                index++;
            }
        }
        dynaClass = new BasicDynaClass(actionName.toString(), null, properties);

        if (singleton) {
            DYNA_ACTION_CLASSES.put(actionName, this);
        }
    }

    public DynaActionDefinition getActionDefinition() {
        return actionDefinition;
    }

    @Override
    public String getName() {
        return dynaClass.getName();
    }

    @Override
    public DynaProperty getDynaProperty(String name) {
        return dynaClass.getDynaProperty(name);
    }

    public ActionProperty getActionProperty(String name) {
        if (!parameterFields.containsKey(name)) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        return parameterFields.get(name).getAnnotation(ActionProperty.class);
    }

    @Override
    public DynaProperty[] getDynaProperties() {
        return dynaClass.getDynaProperties();
    }

    @Override
    public DynaActionBean newInstance() throws ActionCreationException {
        try {
            return new DefaultDynaActionBean(dynaClass.newInstance(), actionDispatch);

        } catch (IllegalAccessException | InstantiationException e) {
            throw new ActionCreationException(e);
        }
    }

    public static ActionName[] actions() {
        List<ActionName> list = new ArrayList<>(DYNA_ACTION_CLASSES.keySet());
        Collections.sort(list);
        return list.toArray(new ActionName[list.size()]);
    }

    public static DynaActionClass get(ActionName actionName) {
        return DYNA_ACTION_CLASSES.get(actionName);
    }

    static class DefaultDynaActionBean extends Action<Object> implements DynaActionBean {
        private DynaBean bean;
        private ActionDispatch actionDispatch;

        DefaultDynaActionBean(DynaBean bean, ActionDispatch actionDispatch) {
            this.bean = bean;
            this.actionDispatch = actionDispatch;
        }

        @Override
        public boolean contains(String name, String key) {
            return bean.contains(name, key);
        }

        @Override
        public Object get(String name) {
            return bean.get(name);
        }

        @Override
        public Object get(String name, int index) {
            return bean.get(name, index);
        }

        @Override
        public Object get(String name, String key) {
            return bean.get(name, key);
        }

        @Override
        public DynaClass getDynaClass() {
            return bean.getDynaClass();
        }

        @Override
        public void remove(String name, String key) {
            bean.remove(name, key);
        }

        @Override
        public void set(String name, Object value) {
            bean.set(name, value);
        }

        @Override
        public void set(String name, int index, Object value) {
            bean.set(name, index, value);
        }

        @Override
        public void set(String name, String key, Object value) {
            bean.set(name, key, value);
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
                    return null;
                }
            }).get();
        }
    }
}
