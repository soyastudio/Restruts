package soya.framework.albertsons.actions.schema;

import org.apache.xmlbeans.SchemaTypeSystem;
import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;
import soya.framework.action.ParameterMapping;
import soya.framework.albertsons.actions.WorkshopAction;
import soya.framework.document.actions.avro.XsdToAvsc;
import soya.framework.document.actions.xmlbeans.xs.XmlBeansUtils;

import java.io.File;

@OperationMapping(domain = "albertsons",
        name = "xsd-to-avsc-transform",
        path = "/workshop/cmm/xsd-to-avsc",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class XsdToAvscAction extends WorkshopAction<String> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String cmm;

    @Override
    public String execute() throws Exception {
        File file = new File(cmmDir(), "BOD/" + cmm + ".xsd");
        SchemaTypeSystem sts = XmlBeansUtils.getSchemaTypeSystem(file);
        return XsdToAvsc.fromXmlSchema(sts).toString(true);
    }
}
