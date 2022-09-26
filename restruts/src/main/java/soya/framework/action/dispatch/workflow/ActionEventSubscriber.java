package soya.framework.action.dispatch.workflow;

import soya.framework.action.*;
import soya.framework.action.dispatch.ActionDispatch;
import soya.framework.action.dispatch.Evaluator;
import soya.framework.action.dispatch.EventSubscriber;

import java.net.URI;

public class ActionEventSubscriber<T> implements Subscriber<T> {

    private ActionName address;
    private ActionDispatch actionDispatch;
    private ActionName publish;

    public ActionEventSubscriber() {
        EventSubscriber annotation = getClass().getAnnotation(EventSubscriber.class);

        this.address = ActionName.fromURI(URI.create(annotation.subscribe()));
        this.actionDispatch = ActionDispatch.fromAnnotation(annotation.dispatch());
        if(!annotation.publish().isEmpty()) {
            this.publish = ActionName.fromURI(URI.create(annotation.publish()));
        }
    }

    @Override
    public String address() {
        return address.toString();
    }

    @Override
    public void onEvent(Event<T> event) {
        ActionCallable action = actionDispatch.create(event, new Evaluator<Event<T>>() {
            @Override
            public Object evaluate(String expression, Event<T> context) {

                return null;
            }
        });

        ActionResult result = action.call();

        if(publish != null) {
            ActionEventBus actionEventBus = ActionContext.getInstance().getService(ActionEventBus.class);
            actionEventBus.post(new ActionEvent(event, address, result.get()));
        }
    }
}
