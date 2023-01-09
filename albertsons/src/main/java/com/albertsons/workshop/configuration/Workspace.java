package com.albertsons.workshop.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import soya.framework.util.StreamUtils;

import java.io.*;
import java.util.Objects;

public class Workspace {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final String DOCUMENT = "doc";
    public static final String HISTORY = "history";
    public static final String WORK = "work";
    public static final String DEPLOYMENT = "deployment";

    public static final String MAPPING_FILE = "xpath-mappings.xlsx";
    public static final String DEFAULT_MAPPING_SHEET = "Mappings";
    public static final String[] MAPPING_COLUMNS = new String[]{"Target", "DataType", "Cardinality", "Mapping", "Source", "Version"};

    public static final String XPATH_SCHEMA = "xpath-schema.properties";
    public static final String XPATH_MAPPINGS = "xpath-mappings.properties";
    public static final String XPATH_CONSTRUCT = "xpath-construct.properties";

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

        // Documents
        File docDir = new File(dir, DOCUMENT);
        docDir.mkdir();

        // Work
        File workDir = new File(dir, WORK);
        workDir.mkdir();

        File xpathSchema = new File(workDir, XPATH_SCHEMA);
        xpathSchema.createNewFile();

        File xpathMappings = new File(workDir, XPATH_MAPPINGS);
        xpathMappings.createNewFile();

        File xpathConstruct = new File(workDir, XPATH_CONSTRUCT);
        xpathConstruct.createNewFile();

        // Deployments
        File deployDir = new File(dir, DEPLOYMENT);
        deployDir.mkdir();

        // History
        File histDir = new File(dir, HISTORY);
        histDir.mkdir();

        Project project = new Project(name);
        File file = new File(dir, "project.json");
        file.createNewFile();
        StreamUtils.write(GSON.toJson(project), file);

        File readme = new File(dir, "README.md");
        readme.createNewFile();



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
