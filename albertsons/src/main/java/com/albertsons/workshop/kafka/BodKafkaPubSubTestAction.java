package com.albertsons.workshop.kafka;

import com.google.gson.*;
import soya.framework.action.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ActionDefinition(domain = "kafka",
        name = "bod-test",
        path = "/bod-test",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class BodKafkaPubSubTestAction extends KafkaAction<String> {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @ActionProperty(
            parameterType = ActionParameterType.PAYLOAD,
            required = true
    )
    private String testCase;

    @WiredService
    private KafkaPubSubTestRunner kafkaPubSubTestRunner;

    @Override
    public String execute() throws Exception {
        KafkaPubSubTestCase test = GSON.fromJson(testCase, KafkaPubSubTestCase.class);
        return kafkaPubSubTestRunner.execute(test, environment);
    }
}
