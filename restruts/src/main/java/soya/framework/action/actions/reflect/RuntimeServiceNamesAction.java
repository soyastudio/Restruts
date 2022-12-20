package soya.framework.action.actions.reflect;

import soya.framework.action.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ActionDefinition(
        domain = "reflect",
        name = "runtime-service-names",
        path = "/runtime/service-names",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Service Names",
        description = "Print runtime service names."
)
public class RuntimeServiceNamesAction extends Action<String[]> {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            option = "p",
            description = "Prefix for filtering."

    )
    private String prefix;

    @Override
    public String[] execute() throws Exception {

        String[] arr = ActionContext.getInstance().serviceNames();
        if (prefix != null) {
            List<String> list = new ArrayList<>();
            for (String s : arr) {
                if (s.startsWith(prefix)) {
                    list.add(s);
                }
            }
            arr = list.toArray(new String[list.size()]);
        }

        Arrays.sort(arr);

        return arr;
    }
}
