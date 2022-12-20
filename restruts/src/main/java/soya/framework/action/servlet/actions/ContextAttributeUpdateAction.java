package soya.framework.action.servlet.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.ActionParameterType;

@ActionDefinition(
        domain = "web",
        name = "servlet-context-attribute-update",
        path = "/servlet-context/attribute",
        method = ActionDefinition.HttpMethod.PUT,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Service Names",
        description = "Print runtime service names."
)
public class ContextAttributeUpdateAction extends ServletContextAction<Void> {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            option = "a",
            required = true,
            description = {}
    )
    private String attributeName;


    @Override
    public Void execute() throws Exception {
        Object attrValue = servletContext.getAttribute(attributeName);

        servletContext.setAttribute(attributeName, attrValue);

        return null;
    }
}
