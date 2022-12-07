package soya.framework.action.dispatch.proxy;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.commons.util.CodeBuilder;

@ActionDefinition(domain = "dispatch",
        name = "proxy-method-generate",
        path = "/proxy/generate/method",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class ProxyMethodAction extends ProxyInterfaceGenerator {

    @ActionProperty(
            description = {
            },
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            required = true,
            option = "u")
    private String uri;

    @Override
    public String execute() throws Exception {
        CodeBuilder builder = CodeBuilder.newInstance();
        printMethod(uri, builder);
        return builder.toString();
    }

}
