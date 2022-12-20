package soya.framework.albertsons.actions.iib;

import com.google.gson.GsonBuilder;
import com.samskivert.mustache.Mustache;
import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.ActionParameterType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@ActionDefinition(domain = "albertsons",
        name = "version-iib-application",
        path = "/workshop/iib/application/version",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Create IIB Application",
        description = "Create IIB Application based on template.")
public class ApplicationVersionAction extends IIBDevAction<String> {

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM, required = true)
    private String version;

    @Override
    public String execute() throws Exception {
        File dir = new File(iibDevelopmentDir(), application);
        if (dir.exists()) {
            return "Application already exists: " + application;
        }

        dir.mkdirs();

        File workDir = workDir(application);
        if (!workDir.exists()) {
            workDir.mkdir();
        }

        File testDir = testDir(application);
        if (!testDir.exists()) {
            testDir.mkdir();
        }

        // ------------- deployment
        File deployDir = deployDir(application);
        if (!deployDir.exists()) {
            deployDir.mkdir();
            // ----------- ESEDA:
            File esedA = new File(deployDir, "ESEDA");
            esedA.mkdir();
            File overrideDev = new File(esedA, application + ".DV.override.properties");
            overrideDev.createNewFile();
            File overrideQA = new File(esedA, application + ".QA.override.properties");
            overrideQA.createNewFile();
            File overridePR = new File(esedA, application + ".PR.override.properties");
            overridePR.createNewFile();

            // ----------- ESEDA:
            File esedB = new File(deployDir, "ESEDB");
            esedB.mkdir();
            File deployDV = new File(esedB, application + ".DV.deploy.properties");
            deployDV.createNewFile();
            File deployQA = new File(esedB, application + ".QA.deploy.properties");
            deployQA.createNewFile();
            File deployPR = new File(esedB, application + ".PR.deploy.properties");
            deployPR.createNewFile();

        }

        File histDir = histDir(application);
        if (!histDir.exists()) {
            histDir.mkdir();
        }

        Map<String, String> data = new HashMap<>();
        data.put("application", application);

        File bod = bod(application);
        if (!bod.exists()) {
            bod.createNewFile();

            BOD model = new BOD(application);
            FileWriter writer = new FileWriter(bod);
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(model));
            writer.flush();
            writer.close();
        }

        File build = build(application);
        if (!build.exists()) {
            build.createNewFile();
        }

        File readme = readme(application);
        if (!readme.exists()) {
            readme.createNewFile();
        }
        InputStream readmeInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("mustache/readme.mustache");
        String result = Mustache.compiler().compile(new InputStreamReader(readmeInputStream)).execute(data);

        Writer readmeWriter = new FileWriter(readme);
        readmeWriter.write(result);
        readmeWriter.close();

        return result;
    }
}
