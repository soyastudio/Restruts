package soya.framework.action.actions.reflect;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(
        domain = "reflect",
        name = "runtime-stack-traces",
        path = "/runtime/stack-traces",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Runtime Stack Traces",
        description = "List runtime stack traces."
)
public class RuntimeStackTracesAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        JsonObject jsonObject = new JsonObject();
        Thread.getAllStackTraces().entrySet().forEach(e -> {
            JsonArray array = new JsonArray();
            for (StackTraceElement element : e.getValue()) {
                array.add(element.toString());
            }

            jsonObject.add(e.getKey().getName(), array);
        });

        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
    }
}
