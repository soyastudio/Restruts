package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;
import soya.framework.action.Resources;

@ActionDefinition(domain = "dispatch",
        name = "about-dispatch",
        path = "/about",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Overview of action dispatch patterns")
public class AboutDispatchAction extends Action<String> {
    private static final String uri = "classpath://META-INF/soya/framework/action/dispatch/dispatch.md";

    @Override
    public String execute() throws Exception {
        return Resources.getResourceAsString(uri);
    }
}
