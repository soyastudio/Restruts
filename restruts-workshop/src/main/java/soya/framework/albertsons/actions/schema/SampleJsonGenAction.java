package soya.framework.albertsons.actions.schema;

import com.google.gson.JsonParser;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.xmlbeans.SchemaTypeSystem;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.albertsons.actions.WorkshopAction;
import soya.framework.document.actions.avro.SampleAvroGenerator;
import soya.framework.document.actions.avro.XsdToAvsc;
import soya.framework.document.actions.xmlbeans.xs.XmlBeansUtils;

import java.io.File;
import java.util.Random;

@ActionDefinition(domain = "albertsons",
        name = "sample-json",
        path = "/workshop/cmm/sample-json",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class SampleJsonGenAction extends WorkshopAction<String> {

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM, required = true)
    private String cmm;

    @Override
    public String execute() throws Exception {
        File file = new File(cmmDir(), "BOD/" + cmm + ".xsd");
        SchemaTypeSystem sts = XmlBeansUtils.getSchemaTypeSystem(file);
        Schema schema = XsdToAvsc.fromXmlSchema(sts);


        Object result = new SampleAvroGenerator(schema, new Random(), 0).generate();
        GenericRecord genericRecord = (GenericRecord) result;

        return GSON.toJson(JsonParser.parseString(genericRecord.toString()));
    }
}
