package soya.framework.restruts.actions;

import soya.framework.restruts.action.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@OperationMapping(api = "Encoding",
        path = "/encoding/base64-encoding",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class Base64EncodeAction implements Action<String> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM)
    protected String xencoding;

    @PayloadMapping(description = "text for encoding", consumes = MediaType.TEXT_PLAIN)
    private String text;

    @Override
    public String execute() throws Exception {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }
}
