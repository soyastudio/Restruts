package soya.framework.restruts.actions.albertsons.transform;

import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;
import soya.framework.restruts.action.PayloadMapping;

@OperationMapping(domain = "albertsons",
        name = "xml-to-json-transform",
        path = "/workshop/transform/xml-to-json",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class XmlToJsonTransformAction extends TransformAction {

    @PayloadMapping(consumes = MediaType.APPLICATION_XML, description = "XML to Json converter.")
    private String message;

    @Override
    public String execute() throws Exception {
        return null;
    }
}
