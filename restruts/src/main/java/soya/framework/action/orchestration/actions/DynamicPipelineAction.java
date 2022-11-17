package soya.framework.action.orchestration.actions;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

@ActionDefinition(
        domain = "orchestration",
        name = "pipeline",
        path = "/pipeline",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Pipeline",
        description = {
                "Pipeline"
        }
)
public class DynamicPipelineAction extends Action<String> {

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.PAYLOAD
    )
    private String message;

    @Override
    public String execute() throws Exception {
        return null;
    }
}
