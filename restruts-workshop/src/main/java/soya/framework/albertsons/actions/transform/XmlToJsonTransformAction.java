package soya.framework.albertsons.actions.transform;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "albertsons",
        name = "xml-to-json-transform",
        path = "/workshop/transform/xml-to-json",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class XmlToJsonTransformAction extends TransformAction {

    @ActionProperty(parameterType = ActionProperty.PropertyType.PAYLOAD,
            description = "Text for converting",
            contentType = MediaType.APPLICATION_XML,
            required = true)
    private String message;

    @Override
    public String execute() throws Exception {
        return null;
    }
}
