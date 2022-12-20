package soya.framework.action.actions.reflect;

import soya.framework.action.*;

@ActionDefinition(
        domain = "reflect",
        name = "abc",
        path = "/abc/xyz-{pathParam}-tuv/a",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "API",
        description = "Print action apis in markdown format."
)
public class AbcAction extends Action<String> {

    @ActionProperty(
            parameterType = ActionParameterType.PATH_PARAM,
            required = true,
            option = "k",
            description = {
                    "Keyword in action name"
            }
    )
    private String pathParam;

    @Override
    public String execute() throws Exception {
        return pathParam;
    }
}
