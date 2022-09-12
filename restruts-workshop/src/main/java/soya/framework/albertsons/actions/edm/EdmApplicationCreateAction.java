package soya.framework.albertsons.actions.edm;

import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;

import java.io.File;
import java.io.FileWriter;

@OperationMapping(domain = "albertsons",
        name = "edm-application-create",
        path = "/workshop/edm/application",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON,
        displayName = "EDM Application",
        description = "Display EDM application information, or EDM application list if application is not specified.")
public class EdmApplicationCreateAction extends EdmApplicationAction<String> {


    @Override
    public String execute() throws Exception {
        File dir = new File(getEdmDevelopmentDir(), application);
        dir.mkdir();

        String result = null;
        File edmAppFile = edmAppFile(application);
        if(!edmAppFile.exists()) {
            edmAppFile.createNewFile();

            EdmProject project = new EdmProject(application);
            result = GSON.toJson(project);
            FileWriter writer = new FileWriter(edmAppFile);
            writer.write(result);
            writer.flush();
            writer.close();

        }

        return result;
    }
}
