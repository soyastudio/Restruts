package soya.framework.about;

import soya.framework.action.*;

@ActionDefinition(domain = "about",
        name = "article-read-action",
        path = "/article",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Article",
        description = "Print article.")
public class ArticleReadAction extends Action<String> {

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM, required = true, option = "l")
    private String link;

    @Override
    public String execute() throws Exception {


        return Resources.getResourceAsString(link);
    }
}
