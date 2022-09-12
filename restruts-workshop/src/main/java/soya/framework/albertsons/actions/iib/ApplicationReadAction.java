package soya.framework.albertsons.actions.iib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;

import java.io.File;
import java.io.FileReader;

@OperationMapping(domain = "albertsons",
        name = "read-iib-application",
        path = "/workshop/iib/application",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Create IIB Application",
        description = "Create IIB Application based on template.")
public class ApplicationReadAction extends IIBDevAction<String> {

    @Override
    public String execute() throws Exception {
        File dir = new File(iibDevelopmentDir(), application);
        if (!dir.exists()) {
            return "Application does not exists: " + application;
        }

        File bod = bod(application);
        String result = "";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (!bod.exists()) {
            return "File 'bod.json' does not exists: " + application;

        } else {
            FileReader reader = new FileReader(bod);
            result = gson.toJson(JsonParser.parseReader(reader));
            reader.close();
        }

        return result;
    }
}
