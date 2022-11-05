package soya.framework.action.orchestration;

import soya.framework.action.Action;
import soya.framework.action.ActionCallable;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionName;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public abstract class CompositeTaskAction<T> extends Action<T> {

    protected ProcessSession newSession() {
        return new ChoiceAction.Session(this);
    }

    protected static class Session implements ProcessSession {

        private final String name;
        private final String id;
        private final long createdTime;

        private Map<String, Object> parameters = new LinkedHashMap<>();
        private Map<String, Object> attributes = new HashMap<>();

        Session(ActionCallable action) {
            ActionDefinition actionDefinition = action.getClass().getAnnotation(ActionDefinition.class);

            this.name = ActionName.create(actionDefinition.domain(), actionDefinition.name()).toString();
            this.id = UUID.randomUUID().toString();
            this.createdTime = System.currentTimeMillis();

        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getCreatedTime() {
            return createdTime;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String[] parameterNames() {
            return new String[0];
        }

        @Override
        public Object parameterValue(String paramName) {
            return null;
        }

        @Override
        public String[] attributeNames() {
            return new String[0];
        }

        @Override
        public Object get(String attrName) {
            return null;
        }

        @Override
        public void set(String attrName, Object attrValue) {

        }
    }
}
