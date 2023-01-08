package soya.framework.action.orchestration.actions;

import soya.framework.action.*;
import soya.framework.action.orchestration.eventbus.ActionEvent;
import soya.framework.action.orchestration.eventbus.ActionEventBus;
import soya.framework.action.orchestration.eventbus.Event;

@ActionDefinition(domain = "orchestration",
        name = "event-publisher",
        path = "/eventbus/publisher",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Pipeline Dispatch",
        description = "Generic pipeline dispatch action")
public class EventPublishAction extends Action<String> {

    @ActionProperty(description = {

    },
            parameterType = ActionParameterType.HEADER_PARAM,
            required = true,
            option = "a")
    private String address;

    @ActionProperty(description = {

    },
            parameterType = ActionParameterType.PAYLOAD,
            required = true,
            option = "p")
    private String payload;

    @Override
    public String execute() throws Exception {
        Event event = new ActionEvent(this, address, payload);
        context().getService(ActionEventBus.class).dispatch(event);
        return event.getId();
    }


}
