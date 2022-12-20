package soya.framework.action.actions.reflect;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import soya.framework.action.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ActionDefinition(
        domain = "reflect",
        name = "runtime-service-interface",
        path = "/runtime/service-interface",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Service Details",
        description = "Print runtime service detail information."
)
public class RuntimeServiceInterfaceAction extends Action<String> {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            required = true,
            option = "i",
            description = "Service interface class name."
    )
    private String serviceInterface;

    @Override
    public String execute() throws Exception {
        Class<?> cls = Class.forName(serviceInterface);
        Map<String, ?> services = ActionContext.getInstance().getServices(cls);
        if(services.size() > 0) {
            ServiceInterfaces result = new ServiceInterfaces();
            result.serviceInterface = serviceInterface;
            for(Method method: cls.getDeclaredMethods()) {
                result.methods.add(new MethodInfo(method));
            }

            services.entrySet().forEach(e -> {
                result.services.put(e.getKey(), e.getValue().getClass().getName());
            });

            return new GsonBuilder().setPrettyPrinting().create().toJson(result);


        } else {
            return JsonNull.INSTANCE.toString();
        }
    }

    protected boolean callable(Method method) {
        if(!Serializable.class.isAssignableFrom(method.getReturnType())) {
            return false;
        }

        for(Parameter parameter : method.getParameters()) {
            if(!Serializable.class.isAssignableFrom(parameter.getType())) {
                return false;
            }
        }

        return true;
    }

    static class ServiceInterfaces {
        private String serviceInterface;
        private List<MethodInfo> methods = new ArrayList<>();
        private Map<String, String> services = new LinkedHashMap<>();
    }

    static class MethodInfo {
        private final String name;
        private final String[] parameterTypes;
        private final String returnType;

        public MethodInfo(Method method) {
            this.name = method.getName();
            this.returnType = method.getReturnType().getName();

            List<String> parameters = new ArrayList<>();
            for (Parameter parameter: method.getParameters()) {
                parameters.add(parameter.getType().getName());
            }

            this.parameterTypes = parameters.toArray(new String[parameters.size()]);

        }
    }
}
