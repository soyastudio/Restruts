package soya.framework.action.dispatch;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import soya.framework.action.*;

@ActionDefinition(domain = "dispatch",
        name = "generic-dispatch",
        path = "/dispatch/action",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Dispatch",
        description = "Print as markdown format.")
public class GenericDispatchAction extends Action<Object> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String command;

    @ActionProperty(parameterType = ActionProperty.PropertyType.PAYLOAD, contentType = MediaType.APPLICATION_JSON, required = true)
    private String payload;

    @Override
    public Object execute() throws Exception {
        JsonObject jsonObject = payload == null? new JsonObject() : JsonParser.parseString(payload).getAsJsonObject();
        ActionDispatch dispatch = ActionDispatch.fromURI(command);

        ActionCallable action = dispatch.create(jsonObject, (expression, context) -> {
            return jsonObject.get(expression).getAsString();
        });

        return action.call().get();
    }
}
