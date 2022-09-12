package soya.framework.albertsons.actions.edm;

import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;
import soya.framework.action.ParameterMapping;

@OperationMapping(domain = "albertsons",
        name = "edm-table-mapping",
        path = "/workshop/edm/table-mapping",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class EdmTableMappingAction extends EdmAction<String> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String table;

    @Override
    public String execute() throws Exception {
        return GSON.toJson(tableMapping(table));
    }
}
