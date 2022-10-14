package soya.framework.action.dispatch;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;
import soya.framework.common.util.CodeBuilder;

@ActionDefinition(domain = "dispatch",
        name = "generator-dispatch-action-class",
        path = "/generator/dispatch-action-class",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Dispatch Action Class Generator",
        description = "Print as markdown format.")
public class ActionDispatchClassGenerator extends DispatchClassGenerator {

    @Override
    public String execute() throws Exception {
        CodeBuilder builder = CodeBuilder.newInstance();
        printPackage(packageName, builder);

        classStatementStart(className, builder);

        classStatementEnd(builder);

        return builder.toString();
    }
}
