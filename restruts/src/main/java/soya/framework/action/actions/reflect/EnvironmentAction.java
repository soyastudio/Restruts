package soya.framework.action.actions.reflect;

import soya.framework.action.Action;
import soya.framework.action.ActionContext;
import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        List<String> propNames = new ArrayList<>(properties.keySet());
        Collections.sort(propNames);

        propNames.forEach(e -> {
            builder.append(e).append("=").append(properties.get(e)).append("\n");
        });

        return builder.toString();
    }
}
