package com.albertsons.workshop.kafka;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "kafka",
        name = "test",
        path = "/test",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON)
public class BodKafkaTestAction extends KafkaAction<String> {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            required = true,
            displayOrder = 2
    )
    private String businessObject;

    @Override
    public String execute() throws Exception {
        return null;
    }
}
