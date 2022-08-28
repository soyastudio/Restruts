package soya.framework.restruts.action;

public class TestAction implements Action<String> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String message;

    @Override
    public String execute() throws Exception {
        return message;
    }
}
