package com.albertsons.workshop.kafka;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "kafka",
        name = "consume-seek-to-end",
        path = "/consume/seek-to-end",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class ConsumeSeekToEndAction extends ConsumeAction<String> {

    @Override
    public String execute() throws Exception {
        return null;
    }
}
