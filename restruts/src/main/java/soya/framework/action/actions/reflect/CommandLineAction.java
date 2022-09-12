package soya.framework.action.actions.reflect;

import soya.framework.action.*;

@OperationMapping(domain = "about",
        name = "commandline",
        path = "/command",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "About",
        description = "Print as markdown format.")
public class CommandLineAction extends Action<String> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String command;

    @PayloadMapping(consumes = MediaType.APPLICATION_JSON, description = "")
    private String payload;

    @Override
    public String execute() throws Exception {

        return null;
    }
}
