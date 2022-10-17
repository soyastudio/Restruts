package soya.framework.about;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;
import soya.framework.action.Resources;

@ActionDefinition(domain = "about",
        name = "index",
        path = "/",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Index",
        description = "Index of  articles.")
public class IndexAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        return Resources.getResourceAsString("classpath://META-INF/index.md");
    }
}
