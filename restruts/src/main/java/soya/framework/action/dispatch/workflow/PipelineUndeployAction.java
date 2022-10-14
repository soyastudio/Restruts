package soya.framework.action.dispatch.workflow;

import soya.framework.action.*;

@ActionDefinition(domain = "dispatch",
        name = "pipeline-undeploy",
        path = "/pipeline",
        method = ActionDefinition.HttpMethod.DELETE,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Generic Pipeline Dispatch",
        description = "Generic pipeline dispatch action")
public class PipelineUndeployAction extends PipelineAdminAction<Boolean> {
    @ActionProperty(description = {

    },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "p")
    private String pipeline;

    @Override
    public Boolean execute() throws Exception {
        container().undeploy(pipeline);
        return true;
    }
}
