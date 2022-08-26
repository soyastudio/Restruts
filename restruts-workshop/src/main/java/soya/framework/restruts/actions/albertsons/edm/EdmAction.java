package soya.framework.restruts.actions.albertsons.edm;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import soya.framework.restruts.actions.albertsons.WorkshopAction;
import soya.framework.restruts.actions.poi.XlsxDynaClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class EdmAction<T> extends WorkshopAction<T> {

    protected static String EDM_MASTER_MAPPING_FILE = "EDM Master Mapping.xlsx";

    protected File edmDir() {
        return new File(workspace(), "EDM");
    }

    protected File edmMasterMappingFile() {
        return new File(edmDir(), EDM_MASTER_MAPPING_FILE);
    }

    protected XlsxDynaClass allTables() {
        return new XlsxDynaClass(edmMasterMappingFile(), "All Tables");
    }

    protected XlsxDynaClass masterMapping() {
        return new XlsxDynaClass(edmMasterMappingFile(), "MasterMapping");
    }

    protected void properties(XlsxDynaClass dynaClass) {
        for (DynaProperty property : dynaClass.getDynaProperties()) {
            System.out.println("------------ " + property.getName());
        }
    }

    protected TableMapping tableMapping(String table) {
        TableMapping tableMapping = new TableMapping(table);

        for (DynaBean bean : masterMapping().getBeans()) {
            if (table.equalsIgnoreCase((String)bean.get("bim_table_name")) && bean.get("bim_column_name") != null) {
                ColumnMapping columnMapping = new ColumnMapping();
                columnMapping.columnName = (String) bean.get("bim_column_name");
                columnMapping.columnDataType = (String) bean.get("bim_column_data_type");
                columnMapping.mapping = (String) bean.get("bod_xpath/json_path");

                tableMapping.columnMappings.add(columnMapping);
            }
        }

        return tableMapping;
    }


    static class TableMapping {
        String table;
        List<ColumnMapping> columnMappings = new ArrayList<>();

        public TableMapping(String table) {
            this.table = table;
        }
    }

    static class ColumnMapping {
        String columnName;
        String columnDataType;
        String mapping;

    }

}
