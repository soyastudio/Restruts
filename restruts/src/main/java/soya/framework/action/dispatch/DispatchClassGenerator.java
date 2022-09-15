package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionProperty;
import soya.framework.common.util.CodeBuilder;

public abstract class DispatchClassGenerator extends Action<String> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    protected String packageName;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    protected String className;

    @Override
    public String execute() throws Exception {
        CodeBuilder builder = CodeBuilder.newInstance();
        printPackage(packageName, builder);

        classStatementStart(className, builder);

        classStatementEnd(builder);

        return builder.toString();
    }

    protected void printPackage(String packageName, CodeBuilder builder) {
        builder.append("package ").append(packageName).appendLine(";");
        builder.appendLine();
    }

    protected void classStatementStart(String className, CodeBuilder builder) {
        builder.append("public class ").append(className).appendLine(" {");
        builder.appendLine();
    }

    protected void classStatementEnd(CodeBuilder builder) {
        builder.append("}");
    }

}