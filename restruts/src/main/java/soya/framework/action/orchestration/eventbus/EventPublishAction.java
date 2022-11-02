package soya.framework.action.orchestration.eventbus;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "event-publisher",
        path = "/eventbus/publisher",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Pipeline Dispatch",
        description = "Generic pipeline dispatch action")
public class EventPublishAction extends Action<String> {

    @ActionProperty(description = {

    },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "a")
    private String address;

    @ActionProperty(description = {

    },
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            required = true,
            option = "p")
    private String payload;

    @Override
    public String execute() throws Exception {
        Event event = new Event(this, address, payload);
        context().getService(ActionEventBus.class).dispatch(event);
        return event.getId();
    }


}
