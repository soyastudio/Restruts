package soya.framework.restruts.action.actions.about;

import soya.framework.restruts.action.Action;
import soya.framework.restruts.action.ActionContext;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;

import java.util.Map;

@OperationMapping(domain = "about",
        name = "environment",
        path = "/about/environment",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Environment",
        description = "Print environment properties.")
public class EnvironmentAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        Map<String, String> properties = ActionContext.getInstance().properties();
        StringBuilder builder = new StringBuilder();
        properties.entrySet().forEach(e -> {
            builder.append(e.getKey()).append("=").append(e.getValue()).append("\n");
        });

        return builder.toString();
    }
}
