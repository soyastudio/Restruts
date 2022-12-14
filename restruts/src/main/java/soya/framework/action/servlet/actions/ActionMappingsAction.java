package soya.framework.action.servlet.actions;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;
import soya.framework.action.WiredService;
import soya.framework.action.servlet.ActionMappings;

import java.util.Date;

@ActionDefinition(
        domain = "web",
        name = "touch-action-mappings",
        path = "/action-mappings/touch",
        method = ActionDefinition.HttpMethod.PUT,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Service Names",
        description = "Print runtime service names."
)
public class ActionMappingsAction extends Action<String> {

    @WiredService
    private ActionMappings actionMappings;

    @Override
    public String execute() throws Exception {
        actionMappings.touch();
        return new Date(actionMappings.getLastUpdateTime()).toString();
    }
}
