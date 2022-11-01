package soya.framework.action.orchestration.pipeline;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import soya.framework.action.*;

@ActionDefinition(domain = "dispatch",
        name = "pipeline-invoker",
        path = "/pipeline/invoke",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Pipeline Dispatch",
        description = "Generic pipeline dispatch action")
public class PipelineInvokeAction extends Action<Object> {

    @ActionProperty(description = {

    },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "p")
    private String pipeline;

    @ActionProperty(description = {

    },
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            contentType = MediaType.APPLICATION_JSON,
            required = true,
            option = "m")
    private String data;


    @Override
    public Object execute() throws Exception {

        Pipeline ppln = null;
        int index = pipeline.lastIndexOf('.');
        if(index > 0) {
            String ext = pipeline.substring(index).toLowerCase();
            if(ext.equals(".json")) {
                ppln = Pipeline.fromJson(Resources.getResourceAsString(this.pipeline));

            } else if(ext.equals("yaml") || ext.equals("yml")) {
                ppln = Pipeline.fromYaml(Resources.getResourceAsString(this.pipeline));
            }
        }

        if(ppln == null) {
            throw new IllegalArgumentException("Cannot find pipeline from: " + pipeline);
        }

        JsonObject jsonObject = data == null? null: JsonParser.parseString(data).getAsJsonObject();
        return ppln.execute(jsonObject);
    }
}
