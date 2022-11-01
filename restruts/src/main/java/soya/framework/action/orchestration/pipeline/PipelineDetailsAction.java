package soya.framework.action.orchestration.pipeline;

import soya.framework.action.*;

@ActionDefinition(domain = "dispatch",
        name = "pipeline-details",
        path = "/pipeline",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Generic Pipeline Dispatch",
        description = "Generic pipeline dispatch action")
public class PipelineDetailsAction extends Action<String> {

    @ActionProperty(description = {

    },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "p")
    private String pipeline;

    @Override
    public String execute() throws Exception {
        PipelineContainer container = ActionContext.getInstance().getService(PipelineContainer.class);
        return container.pipelineDetails(pipeline);
    }
}
