package soya.framework.action.dispatch.proxy;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.ParameterType;
import soya.framework.commons.util.CodeBuilder;

@ActionDefinition(domain = "dispatch",
        name = "proxy-interface-generate",
        path = "/proxy/generate/interface",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class ProxyInterfaceAction extends ProxyInterfaceGenerator {

    @ActionProperty(
            description = {
            },
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            option = "c")
    private String className;

    @Override
    public String execute() throws Exception {
        CodeBuilder builder = CodeBuilder.newInstance();

        String packageName = className.substring(0, className.lastIndexOf('.'));
        String simpleName = className.substring(packageName.length() + 1);

        printPackage(packageName, builder);
        printImports(builder);
        printInterfaceStart(simpleName, builder);

        builder.appendLine();
        builder.appendLine("// add dispatch method here", 1);
        builder.appendLine();

        printInterfaceEnd(builder);

        return builder.toString();
    }

}
