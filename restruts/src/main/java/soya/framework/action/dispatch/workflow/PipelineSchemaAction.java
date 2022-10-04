package soya.framework.action.dispatch.workflow;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "pipeline-schema",
        path = "/dispatch/pipeline/schema",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Generic Pipeline Dispatch",
        description = "Generic pipeline dispatch action")
public class PipelineSchemaAction extends PipelineAdminAction<String> {

    @ActionProperty(description = {

    },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "p")
    private String pipeline;

    @Override
    public String execute() throws Exception {



        return null;
    }
}
