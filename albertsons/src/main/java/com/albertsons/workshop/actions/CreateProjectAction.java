package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Workspace;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "workshop",
        name = "create-project",
        path = "/project",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON)
public class CreateProjectAction extends ProjectAction {

    @Override
    public String execute() throws Exception {
        workspace.createProject(project);
        return Workspace.GSON.toJson(workspace.getProject(project));
    }
}
