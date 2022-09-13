package soya.framework.albertsons.actions.transform;

import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.PayloadMapping;

@ActionDefinition(domain = "albertsons",
        name = "xml-to-json-transform",
        path = "/workshop/transform/xml-to-json",
        method = ActionDefinition.HttpMethod.POST,
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
