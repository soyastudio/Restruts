package soya.framework.ant.actions;

import soya.framework.action.Action;
import soya.framework.action.ActionProperty;
import soya.framework.ant.ProjectSession;

import java.io.File;

public abstract class AntAction extends Action<Object> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, option = "h")
    protected String home;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, option = "b")
    protected String basedir = ".";

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, option = "o")
    protected String resultName;

    protected ProjectSession project;

    @Override
    public Object execute() throws Exception {
        ProjectSession project = createProjectSession();
        configure(project);
        execute(project);

        if (resultName != null) {
            return project.getResult(resultName);
        } else {
            return project.printEvents();
        }
    }

    protected ProjectSession createProjectSession() {
        File antHome = new File(home);
        if (!antHome.exists()) {
            throw new IllegalArgumentException("Ant home does not exist.");
        }

        File workDir = null;
        if (basedir == null) {
            workDir = antHome;

        } else {
            workDir = new File(antHome, basedir);
            if (!workDir.exists()) {
                workDir.mkdirs();
            }
        }

        return new ProjectSession(workDir);
    }

    protected abstract void configure(ProjectSession project) throws Exception;

    protected abstract void execute(ProjectSession project) throws Exception;

}
