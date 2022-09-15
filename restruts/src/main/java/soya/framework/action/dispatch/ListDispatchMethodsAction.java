package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "find-dispatch-methods",
        path = "/dispatch/dispatch-methods",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Method Dispatch Template",
        description = "Print as markdown format.")
public class ListDispatchMethodsAction extends Action<Object> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String query;

    @Override
    public Object execute() throws Exception {

        Class<?> cls = null;
        try {
            cls = Class.forName(query);

        } catch (Exception e) {

        }


        return new Object();
    }


}
