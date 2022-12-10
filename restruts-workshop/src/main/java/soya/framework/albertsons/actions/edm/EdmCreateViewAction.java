package soya.framework.albertsons.actions.edm;

import soya.framework.action.*;
import soya.framework.commons.util.CodeBuilder;

@ActionDefinition(domain = "albertsons",
        name = "edm-create-view",
        path = "/workshop/edm/view",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Create View",
        description = "EDM Create View")
public class EdmCreateViewAction extends Action<String> {

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true, displayOrder = 1)
    private String view;


    @Override
    public String execute() throws Exception {
        CodeBuilder builder = CodeBuilder.newInstance();

        builder.append("create view ").append(view).appendLine( " (");
        builder.appendLine(")", 2);

        builder.appendLine("as", 1);

        builder.appendLine("select ", 1);

        builder.append("from ", 1);

        return builder.toString();
    }
}
