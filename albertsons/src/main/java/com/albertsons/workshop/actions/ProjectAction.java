package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Project;
import org.apache.xmlbeans.XmlException;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.xmlbeans.XmlSchemaTree;

import java.io.File;
import java.io.IOException;

public abstract class ProjectAction extends WorkshopAction<String> {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            required = true,
            displayOrder = 1
    )
    protected String project;

    protected File getProjectDir() {
        return new File(workspace.getProjectHome(), project);
    }

    protected Project getProject() throws IOException {
        return workspace.getProject(project);
    }

    protected XmlSchemaTree schemaTree() throws XmlException, IOException {
        File file = new File(workspace.getCmmHome(), getProject().getSchemaFile());
        return  new XmlSchemaTree(file);
    }
}
