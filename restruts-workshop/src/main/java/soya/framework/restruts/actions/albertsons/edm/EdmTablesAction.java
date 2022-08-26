package soya.framework.restruts.actions.albertsons.edm;

import com.google.gson.GsonBuilder;
import org.apache.commons.beanutils.DynaBean;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;
import soya.framework.restruts.action.ParameterMapping;
import soya.framework.restruts.actions.poi.XlsxDynaClass;

import java.util.*;

@OperationMapping(domain = "albertsons",
        name = "edm-tables",
        path = "/workshop/edm/tables",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class EdmTablesAction extends EdmAction<String> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM)
    private String bod;

    @Override
    public String execute() throws Exception {

        Set<String> tables = new HashSet<>();
        XlsxDynaClass masterMapping = masterMapping();

        if(bod != null) {
            for(DynaBean bean : masterMapping.getBeans()) {
                String bodName = (String) bean.get("bod_name_(formula)");
                String tbl = (String) bean.get("bim_table_name");
                if(bodName != null && bod.equals(bodName.trim()) && tbl != null && tbl.trim().length() > 0) {
                    tables.add(tbl);
                }
            }
        } else {
            for(DynaBean bean : masterMapping.getBeans()) {
                String tbl = (String) bean.get("bim_table_name");
                if(tbl != null && tbl.trim().length() > 0) {
                    tables.add(tbl);
                }
            }

        }

        List<String> list = new ArrayList<>(tables);
        Collections.sort(list);

        return new GsonBuilder().setPrettyPrinting().create().toJson(list);
    }
}
