package soya.framework.restruts.actions.albertsons.iib;

import com.google.gson.Gson;
import soya.framework.restruts.action.ParameterMapping;
import soya.framework.restruts.actions.albertsons.WorkshopAction;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public abstract class IIBDevAction<T> extends WorkshopAction<T> {

    public static final String WORK = "work";
    public static final String TEST = "test";
    public static final String DEPLOY = "deployment";
    public static final String HISTORY = "history";
    public static final String BOD_FILE = "bod.json";
    public static final String README_FILE = "README.md";
    public static final String BUILD_FILE = "build.xml";

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    protected String application;

    protected File workDir(String application) {
        File appDir = new File(iibDevelopmentDir(), application);
        return new File(appDir, WORK);
    }

    protected File testDir(String application) {
        File appDir = new File(iibDevelopmentDir(), application);
        return new File(appDir, TEST);
    }

    protected File deployDir(String application) {
        File appDir = new File(iibDevelopmentDir(), application);
        return new File(appDir, DEPLOY);
    }

    protected File histDir(String application) {
        File appDir = new File(iibDevelopmentDir(), application);
        return new File(appDir, HISTORY);
    }

    protected File bod(String application) {
        File appDir = new File(iibDevelopmentDir(), application);
        return new File(appDir, BOD_FILE);
    }

    protected File readme(String application) {
        File appDir = new File(iibDevelopmentDir(), application);
        return new File(appDir, README_FILE);
    }

    protected File build(String application) {
        File appDir = new File(iibDevelopmentDir(), application);
        return new File(appDir, BUILD_FILE);
    }

    protected BOD getBOD(String application) throws IOException {
        FileReader reader = new FileReader(bod(application));
        BOD bod = new Gson().fromJson(reader, BOD.class);
        reader.close();

        return bod;
    }

    static class BOD {
        private String name;
        private String version = "1.0";
        private String cmm;
        private String sources = "{{source}}";
        private String consumer = "EDM";

        private String application = "{{application}}";
        private String packageName = "{{packageName}}";

        private String inboundTopics = "{{inboundTopics}}";
        private String outboundTopics = "{{outboundTopics}}";

        BOD(String name) {
            this.name = name;
            this.cmm = "Get" + name + ".xsd";
        }

        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }

        public String getCmm() {
            return cmm;
        }

        public String getSources() {
            return sources;
        }

        public String getConsumer() {
            return consumer;
        }

        public String getApplication() {
            return application;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getInboundTopics() {
            return inboundTopics;
        }

        public String getOutboundTopics() {
            return outboundTopics;
        }
    }

}
