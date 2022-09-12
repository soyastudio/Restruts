package soya.framework.document.actions.poi;

import soya.framework.document.actions.DocumentAction;
import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;
import soya.framework.action.ParameterMapping;

import java.io.File;

@OperationMapping(domain = "document",
        name = "xlsx-to-json",
        path = "/xlsx/read",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class SpreadSheetAction extends DocumentAction<XlsxDynaClass> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String uri;

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String sheet;

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
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
