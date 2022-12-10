package soya.framework.action.servlet.actions;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

import java.util.Enumeration;

@ActionDefinition(
        domain = "web",
        name = "servlet-context-attributes",
        path = "/servlet-context/attributes",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Service Names",
        description = "Print runtime service names."
)
public class ContextAttributesAction extends WebInfoAction<String>{

    @Override
    public String execute() throws Exception {




        JsonObject jsonObject = new JsonObject();
        Enumeration<String> enumeration = servletContext.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            String attrName = enumeration.nextElement();
            Object attrValue = servletContext.getAttribute(attrName);

            jsonObject.addProperty(attrName, attrValue.getClass().getName());


        }
        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
    }
}
