package soya.framework.action.dispatch;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaProperty;
import soya.framework.action.*;

public abstract class DynaActionClassBase implements DynaActionClass {

    private ActionDescription actionDescription;
    private BasicDynaClass basicDynaClass;

    protected DynaActionClassBase() {

    }

    protected void init(ActionDescription actionDescription, DynaProperty[] properties) {
        this.actionDescription = actionDescription;
        this.basicDynaClass = new BasicDynaClass(actionDescription.getActionName().toString(), null, properties);
    }

    @Override
    public ActionDescription getActionDescription() {
        return actionDescription;
    }

    @Override
    public String getName() {
        return basicDynaClass.getName();
    }

    @Override
    public DynaProperty getDynaProperty(String name) {
        return basicDynaClass.getDynaProperty(name);
    }

    @Override
    public DynaProperty[] getDynaProperties() {
        return basicDynaClass.getDynaProperties();
    }

    protected BasicDynaClass getBasicDynaClass() {
        return basicDynaClass;
    }

    protected abstract static class DynaActionBeanBase<T extends DynaActionClassBase> implements DynaActionBean {

        private T dynaActionClass;
        protected BasicDynaBean bean;

        protected DynaActionBeanBase(T dynaActionClass) throws ActionCreationException {
            this.dynaActionClass = dynaActionClass;
            try {
                this.bean = (BasicDynaBean) dynaActionClass.getBasicDynaClass().newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                throw new ActionCreationException(e);
            }
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
        public T getDynaClass() {
            return dynaActionClass;
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
        public ActionName actionName() {
            return dynaActionClass.getActionDescription().getActionName();
        }

        @Override
        public ActionResult call() {
            try {
                return ActionResults.create(this, execute());
            } catch (Exception e) {
                return ActionResults.create(this, e);
            }
        }

        protected abstract Object execute() throws Exception;
    }

}
