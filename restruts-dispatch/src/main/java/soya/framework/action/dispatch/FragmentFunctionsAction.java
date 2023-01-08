package soya.framework.action.dispatch;

import com.google.gson.GsonBuilder;
import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

import java.util.ArrayList;
import java.util.List;

@ActionDefinition(
        domain = "dispatch",
        name = "fragment-functions",
        path = "/fragment/functions",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "API Index",
        description = "Print action apis index in yaml format."
)
public class FragmentFunctionsAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        List<FunctionModel> functions = new ArrayList<>();
        Fragment.getInstance().getFunctions().entrySet().forEach(e -> {
            FunctionModel model = new FunctionModel();
            model.name = e.getKey();
            model.type = e.getValue().getName();

            functions.add(model);
        });
        return new GsonBuilder().setPrettyPrinting().create().toJson(functions);
    }

    static class FunctionModel {
        private String name;
        private String type;
    }
}
