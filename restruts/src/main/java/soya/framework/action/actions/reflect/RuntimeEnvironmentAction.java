package soya.framework.action.actions.reflect;

import soya.framework.action.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ActionDefinition(
        domain = "reflect",
        name = "runtime-environment",
        path = "/runtime/environment",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Environment",
        description = "Print environment properties."
)
public class RuntimeEnvironmentAction extends Action<String> {

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            option = "p",
            description = "Prefix for filtering."

    )
    private String prefix;

    @Override
    public String execute() throws Exception {
        Map<String, String> properties = ActionContext.getInstance().properties();
        StringBuilder builder = new StringBuilder();
        List<String> propNames = new ArrayList<>(properties.keySet());
        Collections.sort(propNames);

        propNames.forEach(e -> {
            if (prefix == null || e.startsWith(prefix))
                builder.append(e).append("=").append(properties.get(e)).append("\n");
        });

        return builder.toString();
    }
}
