package soya.framework.action.dispatch;

import org.apache.commons.beanutils.DynaProperty;
import soya.framework.action.ActionCreationException;
import soya.framework.action.ActionDescription;
import soya.framework.action.ActionName;
import soya.framework.commons.util.ReflectUtils;
import soya.framework.commons.util.URIUtils;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class DynaCommandDispatchActionClass extends DynaActionClassBase {

    private Class<?> commandType;
    private Method method;

    private Map<String, Assignment> assignments;

    public DynaCommandDispatchActionClass(String dispatch) throws Exception {
        URI uri = URI.create(dispatch);

        ActionName actionName = ActionName.fromURI(uri);
        String path = uri.getPath();

        String className = null;
        String methodName = null;
        StringTokenizer tokenizer = new StringTokenizer(path, "/");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!token.isEmpty()) {
                if (className == null) {
                    className = token;
                } else if (methodName == null) {
                    methodName = token;
                }
            }
        }

        this.commandType = Class.forName(className);
        this.method = ReflectUtils.findMethod(commandType, methodName);

        ActionDescription.Builder builder = ActionDescription.builder()
                .actionType(DynaCommandDispatchActionClass.class.getName())
                .actionName(actionName)
                .httpMethod("POST");

        List<DynaProperty> propertyList = new ArrayList<>();
        if (uri.getQuery() != null) {
            URIUtils.splitQuery(uri.getQuery()).entrySet().forEach(e -> {
                String name = e.getKey();
                String expression = e.getValue().get(0);
                Assignment assignment = new Assignment(expression);
                assignments.put(name, assignment);
                if (AssignmentType.PARAMETER.equals(assignment.getAssignmentType())) {
                    propertyList.add(new DynaProperty(name, String.class));
                    // FIXME:
                    builder.addProperty(null);
                }
            });
        }

        init(builder.create(), propertyList.toArray(new DynaProperty[propertyList.size()]));
    }

    @Override
    public DynaActionBean newInstance() throws ActionCreationException {
        try {
            return new DynaCommandDispatchActionBean(this);
        } catch (Exception e) {
            throw new ActionCreationException(e);
        }
    }

    static class DynaCommandDispatchActionBean extends DynaActionBeanBase<DynaCommandDispatchActionClass> {


        protected DynaCommandDispatchActionBean(DynaCommandDispatchActionClass dynaActionClass) throws ActionCreationException {
            super(dynaActionClass);
        }

        @Override
        protected Object execute() throws Exception {


            return null;
        }
    }
}
