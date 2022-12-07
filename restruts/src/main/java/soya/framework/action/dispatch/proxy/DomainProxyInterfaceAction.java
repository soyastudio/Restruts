package soya.framework.action.dispatch.proxy;

import com.google.common.base.CaseFormat;
import soya.framework.action.*;
import soya.framework.action.dispatch.ActionDispatchPattern;
import soya.framework.action.dispatch.ActionPropertyAssignment;
import soya.framework.action.dispatch.AssignmentType;
import soya.framework.action.dispatch.ParamName;
import soya.framework.commons.util.CodeBuilder;

import java.lang.reflect.Field;

@ActionDefinition(domain = "dispatch",
        name = "proxy-domain-generate",
        path = "/proxy/generate/domain",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class DomainProxyInterfaceAction extends ProxyInterfaceGenerator {

    @ActionProperty(
            description = {
            },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "c")
    private String className;

    @ActionProperty(
            description = {
            },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "d")
    private String domain;

    @Override
    public String execute() throws Exception {
        int lastPoint = className.lastIndexOf('.');
        String packageName = className.substring(0, lastPoint);
        String simpleName = className.substring(lastPoint + 1);


        CodeBuilder builder = CodeBuilder.newInstance();

        printPackage(packageName, builder);
        printImports(builder);

        printInterfaceStart(simpleName, builder);

        ActionName[] actionNames = ActionClass.actions(domain);
        for (ActionName actionName : actionNames) {
            printMethod(ActionClass.get(actionName), builder);
        }

        printInterfaceEnd(builder);

        return builder.toString();
    }
}
