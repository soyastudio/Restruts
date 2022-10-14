package soya.framework.action.dispatch;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

import java.net.URI;

@ActionDefinition(domain = "dispatch",
        name = "generic-action-dispatch",
        path = "/action-dispatch",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class GenericActionDispatchAction extends GenericDispatchAction<Object> {

    @ActionProperty(description = {
            "Action dispatch uri for identifying action with uri format 'domain://name' and assigning action property with query string format such as 'prop1=assign1(exp1)&prop2=assign2(exp2)'.",
            "Here assign() function should be one of val(exp), res(exp), param(exp) or ref(exp):",
            "- val(exp): directly assign property with string value from exp",
            "- res(exp): extract contents from resource uri exp, such as 'classpath://kafka-config.properties'",
            "- param(exp): evaluate value from payload input in json format using expression: exp",
            "- ref(exp): evaluate value from context using expression: exp, available for multiple action dispatch patterns such as pipeline, eventbus etc."
    },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "D")
    protected URI uri;

    @Override
    public Object execute() throws Exception {
        ActionDispatch dispatch = ActionDispatch.fromURI(uri);
        String[] params = dispatch.getParameterNames();
        JsonElement jsonElement = jsonElement();

        JsonObject context = null;
        if (data != null) {
            if (params.length == 1) {
                if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().get(params[0]) != null) {
                    context = jsonElement.getAsJsonObject();

                } else {
                    context = new JsonObject();
                    context.add(params[0], jsonElement);
                }
            } else if (params.length > 1) {
                context = jsonElement.getAsJsonObject();

            }
        }

        return dispatch.create(context).call().get();
    }
}
