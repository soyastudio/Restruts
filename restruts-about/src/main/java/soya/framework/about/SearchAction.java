package soya.framework.about;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "about",
        name = "search",
        path = "/search",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Article",
        description = "Print article.")
public class SearchAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        return "TODO";
    }
}
