package soya.framework.restruts.actions.albertsons.iib;

import org.apache.commons.beanutils.DynaBean;
import soya.framework.commons.util.CodeBuilder;
import soya.framework.restruts.action.ActionExecutor;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;
import soya.framework.restruts.action.ParameterMapping;
import soya.framework.restruts.actions.poi.SpreadSheetAction;
import soya.framework.restruts.actions.poi.XlsxDynaClass;

import java.io.File;

@OperationMapping(domain = "albertsons",
        name = "validate-iib-mappings",
        path = "/workshop/iib/mappings/validate",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        description = "Validate IIB Mapping Sheet.")
public class IIBDevMappingsValidateAction extends IIBDevAction<String> {
    private static final String COLUMNS = "Target, DataType, Cardinality, Mapping, Source, Version";

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String fileName;

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String sheetName;

    @Override
    public String execute() throws Exception {
        File workDir = workDir(application);
        File xlsx = new File(workDir, fileName);

        XlsxDynaClass data = (XlsxDynaClass) ActionExecutor.executor(SpreadSheetAction.class)
                .setProperty("uri", xlsx.toURI().toString())
                .setProperty("sheet", sheetName)
                .setProperty("columns", COLUMNS)
                .execute();

        CodeBuilder builder = CodeBuilder.newInstance();
        data.getBeans().forEach(e -> {
            String target = e.get("Target") == null ? null : e.get("Target").toString();
            String dataType = e.get("DataType") == null ? "type(?)" : "type(" + e.get("DataType") + ")";
            String cardinality = e.get("Cardinality") == null ? "::cardinality(?)" : "::cardinality(" + e.get("Cardinality") + ")";

            if (target != null) {
                builder.append(e.get("Target").toString()).append("=")
                        .append(dataType)
                        .append(cardinality)
                        .append(getMapping(e))
                        .append(getSource(e))
                        .append(getVersion(e))
                        .append("\n");

            }
        });

        return builder.toString();
    }

    private String getMapping(DynaBean bean) {
        if(bean.get("Mapping") != null && bean.get("Mapping").toString().trim().length() > 0) {
            String value = bean.get("Mapping").toString().trim();
            if(value.contains(" ")) {
                return "::rule(?)";
            } else {
                return "::rule(" + value + ")";
            }
        } else {
            return "";
        }
    }

    private String getSource(DynaBean bean) {
        if(bean.get("Source") != null && bean.get("Source").toString().trim().length() > 0) {
            String value = bean.get("Source").toString().trim();
            value = value.replace("/", ".");
            return "::source(" + value + ")";

        } else {
            return "";
        }
    }

    private String getVersion(DynaBean bean) {
        if(bean.get("Version") != null && bean.get("Version").toString().trim().length() > 0) {
            return "::version(" + bean.get("Version") + ")";

        } else {
            return "";
        }
    }


}