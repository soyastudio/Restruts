package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Project;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;

import java.io.IOException;

public abstract class ProjectAction extends WorkshopAction<String> {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            displayOrder = 1
    )
    protected String project;

    protected Project getProject() throws IOException {
        return workspace.getProject(project);
    }
}
