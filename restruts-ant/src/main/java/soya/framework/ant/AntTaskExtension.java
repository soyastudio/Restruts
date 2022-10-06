package soya.framework.ant;

import org.apache.tools.ant.Task;

public class AntTaskExtension extends Task {

    @Override
    public ProjectSession getProject() {
        return (ProjectSession) super.getProject();
    }
}
