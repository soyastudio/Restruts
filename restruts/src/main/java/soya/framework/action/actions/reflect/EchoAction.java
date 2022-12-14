package soya.framework.action.actions.reflect;

import soya.framework.action.*;

@ActionDefinition(
        domain = "reflect",
        name = "util-echo",
        path = "/util/echo",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Echo",
        description = "Print input message directly."
)
public class EchoAction extends Action<String> {

    @ActionProperty(
            parameterType = ActionParameterType.PAYLOAD,
            required = true,
            option = "m",
            description = "Message for echoing."

    )
    protected String message;

    @Override
    public String execute() throws Exception {
        return message;
    }
}
