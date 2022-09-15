package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "generic-pipeline",
        path = "/dispatch/pipeline",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Pipeline",
        description = "Generic pipeline action")
public class GenericPipelineAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        return null;
    }
}
