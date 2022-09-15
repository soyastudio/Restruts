package soya.framework.document.actions.poi;

import soya.framework.document.actions.DocumentAction;
import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;

import java.io.File;

@ActionDefinition(domain = "document",
        name = "xlsx-to-json",
        path = "/xlsx/read",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class SpreadSheetAction extends DocumentAction<XlsxDynaClass> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String uri;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String sheet;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String columns;

    @Override
    public XlsxDynaClass execute() throws Exception {
        File file = getFile(uri);
        String[] properties = columns.split(",");
        for (int i = 0; i < properties.length; i++) {
            properties[i] = properties[i].trim();
        }

        return new XlsxDynaClass(file.getName(), properties, file, sheet);
    }
}
