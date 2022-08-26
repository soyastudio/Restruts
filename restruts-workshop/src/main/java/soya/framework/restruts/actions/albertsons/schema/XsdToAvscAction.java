package soya.framework.restruts.actions.albertsons.schema;

import org.apache.xmlbeans.SchemaTypeSystem;
import soya.framework.commons.knowledge.KnowledgeTree;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;
import soya.framework.restruts.action.ParameterMapping;
import soya.framework.restruts.actions.albertsons.WorkshopAction;
import soya.framework.restruts.actions.avro.XsdToAvsc;
import soya.framework.restruts.actions.xmlbeans.xs.XmlBeansUtils;
import soya.framework.restruts.actions.xmlbeans.xs.XsKnowledgeSystem;
import soya.framework.restruts.actions.xmlbeans.xs.XsNode;

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
