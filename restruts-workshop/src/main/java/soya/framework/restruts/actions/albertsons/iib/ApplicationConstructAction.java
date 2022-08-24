package soya.framework.restruts.actions.albertsons.iib;

import com.samskivert.mustache.Mustache;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;

import java.io.*;

@OperationMapping(domain = "albertsons",
        name = "construct-iib-application",
        path = "/workshop/iib/application/construct",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Construct IIB Application",
        description = "Construct IIB Application based on bod.json.")
public class ApplicationConstructAction extends IIBDevAction<String> {

    @Override
    public String execute() throws Exception {
        File bodFile = bod(application);
        if (!bodFile.exists()) {
            throw new IllegalStateException("File 'bod.json' does not exist: " + application);
        }

        BOD bod = getBOD(application);

        readmeTask(bod);
        workTask(bod);
        testTask(bod);
        deployTask(bod);
        historyTask(bod);

        return "result";
    }

    private void workTask(BOD bod) {

        File workDir = workDir(application);
        if (!workDir.exists()) {
            workDir.mkdir();
        }

    }

    private void testTask(BOD bod) {
        File testDir = testDir(application);
        if (!testDir.exists()) {
            testDir.mkdir();
        }
    }

    private void deployTask(BOD bod) throws IOException {
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
    }

    private void historyTask(BOD bod) {
        File histDir = histDir(application);
        if (!histDir.exists()) {
            histDir.mkdir();
        }

    }

    private void readmeTask(BOD bod) throws IOException {
        File readme = readme(application);
        if (!readme.exists()) {
            readme.createNewFile();
        }
        InputStream readmeInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("mustache/readme.mustache");
        String result = Mustache.compiler().compile(new InputStreamReader(readmeInputStream)).execute(bod);

        Writer readmeWriter = new FileWriter(readme);
        readmeWriter.write(result);
        readmeWriter.close();
    }
}
