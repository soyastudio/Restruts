package soya.framework.ant.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.ParameterType;
import soya.framework.ant.ProjectSession;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@ActionDefinition(domain = "ant",
        name = "build",
        path = "/build",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Ant Build",
        description = "Base64 Encode")
public class BuildAction extends AntAction {

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, option = "t")
    protected String target;

    @ActionProperty(parameterType = ParameterType.PAYLOAD, option = "s", contentType = MediaType.APPLICATION_XML)
    protected String script;

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, option = "l")
    protected boolean printLog;


    @Override
    protected void configure(ProjectSession project) throws Exception {
        if (script != null) {
            project.configure(script);

        } else {
            project.configure(new File(project.getBaseDir(), "build.xml"));
        }
    }

    @Override
    protected void execute(ProjectSession project) throws Exception {
        try {
            if (target != null) {
                project.executeTarget(target);

            } else {
                project.executeTarget(project.getDefaultTarget());

            }
        } catch (Exception ex) {
            throw ex;

        } finally {

            if (printLog) {
                printLog(project);
            }
        }
    }

    protected void printLog(ProjectSession project) throws IOException {
        File logFile = new File(project.getBaseDir(), "build.log");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        Files.write(Paths.get(logFile.toURI()), project.printEvents().getBytes(StandardCharsets.UTF_8));

    }
}
