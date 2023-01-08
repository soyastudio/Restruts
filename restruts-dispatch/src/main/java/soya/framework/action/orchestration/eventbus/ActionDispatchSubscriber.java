package soya.framework.action.orchestration.eventbus;

import soya.framework.action.ActionContext;
import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.ActionDispatch;
import soya.framework.action.dispatch.ActionDispatchPattern;
import soya.framework.action.dispatch.ActionDispatchSession;

public abstract class ActionDispatchSubscriber implements Subscriber {

    protected ActionDispatch actionDispatch;

    public ActionDispatchSubscriber() {
        if (getClass().getAnnotation(ActionDispatchPattern.class) != null) {
            ActionDispatchPattern pattern = getClass().getAnnotation(ActionDispatchPattern.class);
            this.actionDispatch = ActionDispatch.fromURI(pattern.uri());
        }
    }

    @Override
    public void onEvent(Event event) {
        EventDigester digester;
        if (EventDigester.class.isAssignableFrom(getClass())) {
            digester = (EventDigester) this;

        } else {
            digester = getEventDigester();
        }

        ActionDispatchSession session = digester != null ? digester.digest(event) : new EventSession(actionDispatch, event);
        ActionResult result = actionDispatch.dispatch(session);

        if (getClass().getAnnotation(EventPublisher.class) != null) {
            EventPublisher publisher = getClass().getAnnotation(EventPublisher.class);
            ActionContext.getInstance().getService(ActionEventBus.class).dispatch(new ActionEvent(event, publisher.value(), result.get()));
        }
    }

    protected EventDigester getEventDigester() {
        return null;
    }

    static class EventSession implements ActionDispatchSession {
        private ActionDispatch actionDispatch;
        private Event event;

        public EventSession(ActionDispatch actionDispatch, Event event) {
            this.actionDispatch = actionDispatch;
            this.event = event;
        }

        @Override
        public String[] parameterNames() {
            return actionDispatch.getParameterNames();
        }

        @Override
        public Object parameterValue(String paramName) {
            return null;
        }

        @Override
        public Object data() {
            return null;
        }
    }


}
