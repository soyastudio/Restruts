package soya.framework.action.actions.reflect;

import soya.framework.action.*;

import java.util.ArrayList;
import java.util.List;

@ActionDefinition(
        domain = "reflect",
        name = "api-search",
        path = "/api/search",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "API Search",
        description = "Search from action name with provided keyword"
)
public class ApiSearchAction extends Action<String[]> {

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            option = "k",
            description = {
                    "Keyword in action name"
            }
    )
    private String keyword;

    @Override
    public String[] execute() throws Exception {
        String token = keyword.toUpperCase();
        List<String> results = new ArrayList<>();
        ActionName[] actionNames = ActionClass.actions(null);
        for(ActionName name: actionNames) {
            if(name.toString().toUpperCase().contains(token)) {
                results.add(name.toString());
            }
        }
        return results.toArray(new String[results.size()]);
    }
}
