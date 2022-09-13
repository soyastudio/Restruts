package soya.framework.action;

@ActionDefinition(domain = "test",
        name = "Test",
        path = "/test",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "TEST",
        description = "Print as markdown format.")
public class TestAction extends Action<String> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM)
    private String message;

    @Override
    public String execute() throws Exception {
        return message == null ? "Hi" : message;
    }
}
