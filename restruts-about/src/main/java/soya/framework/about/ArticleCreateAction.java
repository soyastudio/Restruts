package soya.framework.about;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "about",
        name = "article-create-action",
        path = "/article",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Article",
        description = "Print article.")
public class ArticleCreateAction extends Action<String> {


    @Override
    public String execute() throws Exception {
        return "TODO";
    }
}
