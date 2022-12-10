package soya.framework.action.actions.reflect;

import soya.framework.action.*;

import java.util.Locale;

@ActionDefinition(
        domain = "reflect",
        name = "util-log",
        path = "/util/log",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Log",
        description = "Log input message, using JUL as default logging system."
)
public class LogAction extends Action<Void> {

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            defaultValue = "INFO",
            option = "l",
            description = {
                    "Logger level:",
                    "- finest: ALL, TRACE, FINEST;",
                    "- fine: DEBUG, FINER, FINE",
                    "- warn: WARN",
                    "- error: ERROR, FATAL, OFF",
                    "- info: default and for other not specified."
            }
    )
    protected String level = "INFO";

    @ActionProperty(
            parameterType = ParameterType.PAYLOAD,
            required = true,
            option = "m",
            description = "Log message."
    )
    protected String message;

    @Override
    public Void execute() throws Exception {

        switch (level.toUpperCase(Locale.ROOT)) {
            case "ALL":
            case "TRACE":
            case "FINEST":
                logger().finest(message);
                break;

            case "DEBUG":
            case "FINER":
            case "FINE":
                logger().fine(message);
                break;

            case "WARN":
                logger().warning(message);
                break;

            case "ERROR":
            case "FATAL":
            case "OFF":
                logger().severe(message);
                break;

            default:
                logger().info(message);
        }

        return null;
    }
}
