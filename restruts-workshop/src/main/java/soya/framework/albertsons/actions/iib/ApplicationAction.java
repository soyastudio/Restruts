package soya.framework.albertsons.actions.iib;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import soya.framework.action.ActionParameterType;
import soya.framework.albertsons.actions.WorkshopAction;
import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@ActionDefinition(domain = "albertsons",
        name = "iib-application",
        path = "/workshop/iib",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "IIB Application",
        description = "Display IIB application information, or IIB application list if application is not specified.")
public class ApplicationAction extends WorkshopAction<String> {

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM)
    protected String application;

    @Override
    public String execute() throws Exception {
        if (application != null) {
            File dir = new File(iibDevelopmentDir(), application);
            File bod = new File(dir, "bod.json");
            if (bod.exists()) {
                return new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseReader(new FileReader(bod)));

            } else {
                return bodList();
            }

        } else {
            return bodList();
        }
    }

    private String bodList() {
        List<String> list = new ArrayList<>();
        File[] files = iibDevelopmentDir().listFiles();
        for (File file : files) {
            if (file.isDirectory() && new File(file, "bod.json").exists()) {
                list.add(file.getName());
            }
        }

        return new GsonBuilder().setPrettyPrinting().create().toJson(list);
    }
}
