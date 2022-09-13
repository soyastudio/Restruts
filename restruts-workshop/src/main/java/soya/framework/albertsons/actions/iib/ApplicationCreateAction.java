package soya.framework.albertsons.actions.iib;

import com.google.gson.JsonParser;
import soya.framework.albertsons.actions.WorkshopAction;
import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@ActionDefinition(domain = "albertsons",
        name = "create-iib-application",
        path = "/workshop/iib/application",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Create IIB Application",
        description = "Create IIB Application based on template.")
public class ApplicationCreateAction extends IIBDevAction<String> {

    @Override
    public String execute() throws Exception {
        File dir = new File(iibDevelopmentDir(), application);
        if (dir.exists()) {
            return "Application already exists: " + application;
        }

        dir.mkdirs();

        File bod = bod(application);
        String result = "";
        if (!bod.exists()) {
            bod.createNewFile();

            BOD model = new BOD(application);
            result = WorkshopAction.GSON.toJson(model);
            FileWriter writer = new FileWriter(bod);
            writer.write(result);
            writer.flush();
            writer.close();

        } else {
            result = WorkshopAction.GSON.toJson(JsonParser.parseReader(new FileReader(bod)));
        }

        return result;
    }
}
