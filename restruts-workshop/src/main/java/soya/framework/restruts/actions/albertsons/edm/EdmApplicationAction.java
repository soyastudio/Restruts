package soya.framework.restruts.actions.albertsons.edm;

import soya.framework.restruts.action.ParameterMapping;

import java.io.File;

public abstract class EdmApplicationAction<T> extends EdmAction<T> {

    public static final String EDM_APP_FILE = "edm.json";

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    protected String application;

    protected File edmAppFile(String application) {
        return new File(getEdmDevelopmentDir(), application + "/" + EDM_APP_FILE);
    }

    static class EdmProject {
        private String name;
        private String mainTable = "{{mainTable}}";
        private String[] referenceTables = new String[] {"{{referenceTable}}"};
        private String[] dependentTables = new String[] {"{{dependentTable}}"};

        public EdmProject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getMainTable() {
            return mainTable;
        }

        public String[] getReferenceTables() {
            return referenceTables;
        }

        public String[] getDependentTables() {
            return dependentTables;
        }
    }

}
