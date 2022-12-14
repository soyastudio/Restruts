package soya.framework.albertsons.actions.schema;

import soya.framework.action.*;
import soya.framework.albertsons.actions.WorkshopAction;
import soya.framework.xmlbeans.XPathSchemaAction;

@ActionDefinition(domain = "albertsons",
        name = "cmm-xpath-schema",
        path = "/workshop/cmm/xpath-schema",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "CMM XPath-Schema Parsing",
        description = "Generate XPath Schema based on CMM XSD file.")
public class IIBDevXPathSchemaAction extends WorkshopAction<String> {

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM, required = true)
    private String cmm;

    @Override
    public String execute() throws Exception {
        String uri = "file:///" + cmmDir().getAbsolutePath().replaceAll("\\\\", "/")
                + "/BOD/"
                + cmm + ".xsd";
        return (String) ActionExecutor.executor(XPathSchemaAction.class).setProperty("uri", uri).execute();
    }
}
