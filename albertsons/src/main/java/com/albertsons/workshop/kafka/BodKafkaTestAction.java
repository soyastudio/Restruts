package com.albertsons.workshop.kafka;

import com.albertsons.workshop.configuration.Workspace;
import com.google.gson.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import soya.framework.action.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

@ActionDefinition(domain = "kafka",
        name = "test",
        path = "/test",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON)
public class BodKafkaTestAction extends KafkaAction<String> {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            required = true
    )
    private String name;

    @ActionProperty(
            parameterType = ActionParameterType.PAYLOAD,
            required = true
    )
    private String testCases;

    @WiredService
    private KafkaPubSubTestRunner kafkaPubSubTestRunner;

    @Override
    public String execute() throws Exception {
        List<KafkaPubSubTestCase> tests = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(testCases);
        if (jsonElement.isJsonObject()) {
            KafkaPubSubTestCase testCase = GSON.fromJson(jsonElement, KafkaPubSubTestCase.class);
            tests.add(testCase);


        } else {
            JsonArray array = jsonElement.getAsJsonArray();
            KafkaPubSubTestCase[] testCases = GSON.fromJson(jsonElement, KafkaPubSubTestCase[].class);
            Arrays.stream(testCases).forEach(testCase -> {
                tests.add(testCase);
            });
        }

        kafkaPubSubTestRunner.runTest(name, tests, environment);

        return name;
    }
}
