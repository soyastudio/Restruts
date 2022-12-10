package soya.framework.action.dispatch.proxy;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.ParameterType;
import soya.framework.commons.util.CodeBuilder;

import java.lang.reflect.Method;

@ActionDefinition(domain = "dispatch",
        name = "proxy-interface",
        path = "/proxy/interface",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class ProxyInterfaceDetailsAction extends ProxyInterfaceGenerator {

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
        Class<?> cls = Class.forName(className);

        printPackage(cls.getPackage().getName(), builder);
        printImports(builder);

        printInterfaceStart(cls.getSimpleName(), builder);

        for (Method method : cls.getDeclaredMethods()) {
            printMethod(method, builder);
        }

            printInterfaceEnd(builder);

        return builder.toString();
    }
}
