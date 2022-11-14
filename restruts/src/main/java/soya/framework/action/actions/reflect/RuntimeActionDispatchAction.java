package soya.framework.action.actions.reflect;

import com.google.gson.GsonBuilder;
import soya.framework.action.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@ActionDefinition(
        domain = "reflect",
        name = "runtime-action-execution",
        path = "/runtime/action-execution",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Runtime Action Execution",
        description = "Runtime Action Execution."
)
public class RuntimeActionDispatchAction extends Action<String> {

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            required = true,
            option = "c",
            description = "Action execution command in yaml, commandline or uri format."

    )
    private String command;

    @Override
    public String execute() throws Exception {

        List<String> list = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new StringReader(command));
        String line = reader.readLine();
        while (line != null) {
            if (!line.trim().isEmpty()) {
                list.add(line);
            }
            line = reader.readLine();
        }

        ActionExecutor executor = null;
        if (list.size() == 0) {
            throw new IllegalArgumentException("Command is empty");

        } else if (list.size() == 1) {
            executor = ActionExecutor.executor(list.get(0));

        } else {
            String actionName = list.get(0).trim();
            if (actionName.endsWith(":")) {
                actionName = actionName.substring(0, actionName.length() - 1);
            }

            final ActionClass actionClass;
            URI uri = URI.create(actionName);

            if (uri.getScheme().equals("class")) {
                try {
                    actionClass = ActionClass.get((Class<? extends ActionCallable>) Class.forName(uri.getHost()));
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            } else {
                actionClass = ActionClass.get(ActionName.fromURI(uri));
            }

            if (actionClass == null) {
                throw new IllegalArgumentException("Cannot find action class from uri: " + uri);
            }

            executor = ActionExecutor.executor(actionClass.getActionType());

            for (int i = 1; i < list.size(); i++) {
                String ln = list.get(i).trim();
                int index = ln.indexOf(':');
                String key = ln.substring(0, index);
                String value = ln.substring(index + 1);
                if (key.startsWith("--")) {
                    key = key.substring(2).trim();

                } else if (key.startsWith("-")) {
                    key = key.substring(1).trim();

                }

                if (value.length() > 1) {
                    if (value.startsWith("\"") && value.endsWith("\"") || value.startsWith("'") && value.endsWith("'")) {
                        value = value.substring(1, value.length() - 1);
                    }
                }

                if (actionClass.getActionField(key) != null) {
                    executor.setProperty(actionClass.getActionField(key).getName(), value);
                }
            }
        }

        Object result = executor.execute();

        if (result instanceof String) {
            return (String) result;
        } else {
            return new GsonBuilder().setPrettyPrinting().create().toJson(result);
        }

    }
}
