package soya.framework.action.dispatch;

import soya.framework.action.*;

@ActionDefinition(
        domain = "dispatch",
        name = "fragment-process",
        path = "/fragment/process",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "API Index",
        description = "Print action apis index in yaml format."
)
public class FragmentProcessAction extends Action<String> {

    @ActionProperty(
            description = {
            },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "f")
    private String fragment;

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            required = true,
            option = "d"
    )
    private String data;

    @Override
    public String execute() throws Exception {

        ActionResult result = new ActionResult() {
            @Override
            public ActionName actionName() {
                return getActionClass().getActionName();
            }

            @Override
            public Object get() {
                return data;
            }

            @Override
            public boolean success() {
                return true;
            }

            @Override
            public boolean empty() {
                return false;
            }
        };

        return Fragment.process(result, fragment).get().toString();
    }

}