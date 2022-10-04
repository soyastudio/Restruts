package soya.framework.action.dispatch.workflow;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "pipelines",
        path = "/dispatch/pipelines",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Generic Pipeline Dispatch",
        description = "Generic pipeline dispatch action")
public class PipelinesAction extends PipelineAdminAction<String[]> {

    @Override
    public String[] execute() throws Exception {
        return container().pipelines();
    }
}
