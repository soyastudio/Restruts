package com.albertsons.workshop.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.util.StreamUtils;

import java.io.File;

@ActionDefinition(domain = "workshop",
        name = "read-project",
        path = "/project",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class ProjectReadAction extends ProjectAction {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            displayOrder = 2
    )
    protected String fileName = "project.json";

    @Override
    public String execute() throws Exception {
        File file = new File(getProjectDir(), fileName);
        return StreamUtils.read(file);
    }
}
