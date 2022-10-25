package soya.framework.action.dispatch.pipeline;

import com.google.gson.Gson;
import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.dispatch.ActionDispatch;

@ActionDefinition(domain = "dispatch",
        name = "generic-pipeline-dispatch",
        path = "/pipeline",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Pipeline Dispatch",
        description = "Generic pipeline dispatch action")
public class GenericPipelineAction extends Action<Object> {

    @ActionProperty(description = {

    },
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            contentType = MediaType.APPLICATION_JSON,
            required = true,
            option = "p")
    private String pipeline;

    @Override
    public Object execute() throws Exception {

        Pipeline.PipelineModel model = new Gson().fromJson(pipeline, Pipeline.PipelineModel.class);
        Pipeline.Builder builder = Pipeline.builder();
        model.getPipeline().parameters().entrySet().forEach(e -> {
            try {
                builder.addParameter(e.getKey(), Class.forName(e.getValue()));
            } catch (ClassNotFoundException ex) {
                throw new IllegalStateException(ex);
            }
        });

        model.getPipeline().tasks().entrySet().forEach(e -> {
            builder.addTask(e.getKey(), ActionDispatch.fromURI(e.getValue()));
        });

        return builder.create().execute(model.getData());
    }
}
