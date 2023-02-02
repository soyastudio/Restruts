package com.albertsons.workshop.kafka;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "kafka",
        name = "consume-seek-to-begin",
        path = "/consume/seek-to-begin",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class ConsumeSeekToBeginAction extends ConsumeAction<String>{

    @Override
    public String execute() throws Exception {
        return null;
    }
}
