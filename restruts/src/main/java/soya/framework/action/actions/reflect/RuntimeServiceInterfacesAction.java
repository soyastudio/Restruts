package soya.framework.action.actions.reflect;

import soya.framework.action.*;

import java.util.*;

@ActionDefinition(
        domain = "reflect",
        name = "runtime-service-interfaces",
        path = "/runtime/service-interfaces",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Service Interfaces",
        description = "Print runtime service interfaces."
)
public class RuntimeServiceInterfacesAction extends Action<String[]> {

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            option = "p",
            required = true,
            description = "Prefix for filtering."

    )
    private String prefix;

    @Override
    public String[] execute() throws Exception {

        Set<String> set = new HashSet<>();
        String[] arr = ActionContext.getInstance().serviceNames();
        for(String name: arr) {
            Object service = ActionContext.getInstance().getService(name);
            Class<?>[] interfaces = service.getClass().getInterfaces();
            for(Class<?> inf: interfaces) {
                if(inf.getName().startsWith(prefix) && inf.getDeclaredMethods().length > 0) {
                    set.add(inf.getName());

                }
            }
        }

        List<String> results = new ArrayList<>(set);
        Collections.sort(results);

        return results.toArray(new String[results.size()]);
    }
}
