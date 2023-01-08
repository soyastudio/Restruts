package com.albertsons.workshop.configuration;

public class Project {

    private String name;
    private String schemaFile;
    private String mappingFile;
    private String defaultMappingSheet;

    private String application;
    private String packageName;

    public Project(String name) {
        this.name = name;
        this.schemaFile = "/BOD/Get" + name + ".xsd";
        this.mappingFile = "/work/xpath-mappings.xlsx";
        this.defaultMappingSheet = "Mappings";

        this.application = "ESED_" + name + "_IH_Publisher";
        this.packageName = "com.albertsons." + name;

    }
}
