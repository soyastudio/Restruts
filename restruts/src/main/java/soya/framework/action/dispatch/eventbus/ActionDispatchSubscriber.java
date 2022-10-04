package soya.framework.action.dispatch.eventbus;

import org.apache.commons.beanutils.PropertyUtils;
import soya.framework.action.ActionContext;
import soya.framework.action.ActionResult;
import soya.framework.action.ConvertUtils;
import soya.framework.action.dispatch.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class ActionDispatchSubscriber implements Subscriber {
    private static Evaluator evaluator;

    static {
        evaluator = new DefaultEvaluator(
                // Reference evaluator
                (assignment, context, type) -> {
                    Event event = (Event) context;

                    return null;
                },

                // Parameter evaluator
                (assignment, context, type) -> {
                    Event event = (Event) context;
                    Object ctx = event.getPayload();

                    String exp = assignment.getExpression();
                    Object value = null;
                    if (ctx instanceof String) {

                    } else if (ctx instanceof Map) {
                        Map<String, Object> map = (Map<String, Object>) ctx;
                        value = map.get(exp);

                    } else {
                        try {
                            value = PropertyUtils.getProperty(ctx, exp);

                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }

                    return ConvertUtils.convert(value, type);
                }
        );
    }

    protected ActionDispatch actionDispatch;

    public ActionDispatchSubscriber() {
        if (getClass().getAnnotation(ActionDispatchPattern.class) != null) {
            ActionDispatchPattern pattern = getClass().getAnnotation(ActionDispatchPattern.class);
            this.actionDispatch = ActionDispatch.fromAnnotation(pattern);
        }
    }

    @Override
    public void onEvent(Event event) {
        EventDigester digester;
        if(EventDigester.class.isAssignableFrom(getClass())) {
            digester = (EventDigester) this;

        } else {
            digester = getEventDigester();
        }

        ActionDispatchSession session = digester != null? digester.digest(event) : new EventSession(actionDispatch, event);
        ActionResult result = actionDispatch.create(session, evaluator).call();

        if (getClass().getAnnotation(EventPublisher.class) != null) {
            EventPublisher publisher = getClass().getAnnotation(EventPublisher.class);
            ActionContext.getInstance().getService(ActionEventBus.class).dispatch(new Event(event, publisher.value(), result.get()));
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
