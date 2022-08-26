package soya.framework.restruts.actions.albertsons.iib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@OperationMapping(domain = "albertsons",
        name = "create-iib-application",
        path = "/workshop/iib/application",
        method = OperationMapping.HttpMethod.POST,
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
            result = GSON.toJson(model);
            FileWriter writer = new FileWriter(bod);
            writer.write(result);
            writer.flush();
            writer.close();

        } else {
            result = GSON.toJson(JsonParser.parseReader(new FileReader(bod)));
        }

        return result;
    }
}
