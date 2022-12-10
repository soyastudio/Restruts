package soya.framework.action.servlet.actions;

import com.google.gson.GsonBuilder;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.ParameterType;

@ActionDefinition(
        domain = "web",
        name = "servlet-context-attribute",
        path = "/servlet-context/attribute",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Service Names",
        description = "Print runtime service names."
)
public class ContextAttributeAction extends WebInfoAction<String> {

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            option = "a",
            required = true,
            description = {}
    )
    private String attributeName;

    @Override
    public String execute() throws Exception {
        Object o = servletContext.getAttribute(attributeName);

        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }
}
