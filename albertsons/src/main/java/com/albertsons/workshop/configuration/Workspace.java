package com.albertsons.workshop.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Objects;

public class Workspace {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final String DOCUMENT = "doc";
    public static final String HISTORY = "history";
    public static final String WORK = "work";
    public static final String DEPLOYMENT = "deployment";

    private File home;
    private File cmmHome;
    private File projectHome;
    private File templateHome;

    public Workspace(File home) {
        this.home = home;
        this.cmmHome = new File(home, "CMM");
        this.projectHome = new File(home, "BOD");
        this.templateHome = new File(home, "TMP");
    }

    public File getHome() {
        return home;
    }

    public File getCmmHome() {
        return cmmHome;
    }

    public File getProjectHome() {
        return projectHome;
    }

    public File getTemplateHome() {
        return templateHome;
    }

    public File findCmmFile(String name) {
        Objects.requireNonNull(name);
        String fileName = name;
        if(!fileName.endsWith(".xsd")) {
            fileName = fileName + ".xsd";
        }

        File file = new File(cmmHome, "BOD/" + fileName);
        return file;
    }

    public Project createProject(String name) throws IOException {
        File dir = new File(projectHome, name);
        if(dir.exists()) {
            throw new IllegalStateException("Project already exist.");
        }

        dir.mkdir();

        File docDir = new File(dir, DOCUMENT);
        docDir.mkdir();

        File workDir = new File(dir, WORK);
        workDir.mkdir();

        File deployDir = new File(dir, DEPLOYMENT);
        deployDir.mkdir();

        File histDir = new File(dir, HISTORY);
        histDir.mkdir();

        Project project = new Project(name);
        File file = new File(dir, "project.json");
        file.createNewFile();
        Writer writer = new FileWriter(file);
        IOUtils.write(GSON.toJson(project), writer);
        writer.flush();
        writer.close();

        return project;
    }

    public Project getProject(String name) throws IOException {
        File dir = new File(projectHome, name);
        File file = new File(dir, "project.json");
        Reader reader = new FileReader(file);
        Project project = GSON.fromJson(reader, Project.class);
        reader.close();
        return project;
    }

}
