package soya.framework.restruts.action;

public class TestAction implements Action<String> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM)
    private String message;

    @Override
    public String execute() throws Exception {
        System.out.println("========================== message: " + message);

        return "Hello World!";
    }
}
