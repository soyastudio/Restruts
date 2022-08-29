package soya.framework.restruts.actions.albertsons.schema;

import soya.framework.restruts.action.ActionExecutor;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;
import soya.framework.restruts.action.ParameterMapping;
import soya.framework.restruts.actions.albertsons.WorkshopAction;
import soya.framework.restruts.actions.xmlbeans.XPathSchemaAction;

@OperationMapping(domain = "albertsons",
        name = "cmm-xpath-schema",
        path = "/workshop/cmm/xpath-schema",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "CMM XPath-Schema Parsing",
        description = "Generate XPath Schema based on CMM XSD file.")
public class IIBDevXPathSchemaAction extends WorkshopAction<String> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String cmm;

    @Override
    public String execute() throws Exception {
        String uri = "file:///" + cmmDir().getAbsolutePath().replaceAll("\\\\", "/")
                + "/BOD/"
                + cmm + ".xsd";
        return (String) ActionExecutor.executor(XPathSchemaAction.class).setProperty("uri", uri).execute();
    }
}