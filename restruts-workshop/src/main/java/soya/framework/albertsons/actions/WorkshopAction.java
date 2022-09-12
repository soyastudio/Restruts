package soya.framework.albertsons.actions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import soya.framework.action.Action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class WorkshopAction<T> extends Action<T> {
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final File WORKSPACE_HOME;

    private static final File CMM_DIR;
    private static final File IIB_DEVELOPMENT_DIR;
    private static final File EDM_DEVELOPMENT_DIR;

    static {
        File INSTALL_HOME = new File(System.getProperty("workspace.home"));
        File configFile = new File(INSTALL_HOME, "workspace.properties");
        if(configFile.exists()) {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(configFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
            WORKSPACE_HOME = new File(properties.getProperty("workspace.home"));

        } else {
            WORKSPACE_HOME = INSTALL_HOME;
        }

        CMM_DIR = new File(WORKSPACE_HOME, "CMM");
        IIB_DEVELOPMENT_DIR = new File(WORKSPACE_HOME, "ESED_IIB_Development");
        EDM_DEVELOPMENT_DIR = new File(WORKSPACE_HOME, "EDM_Development");
    }

    protected File workspace() {
        return WORKSPACE_HOME;
    }

    protected File cmmDir() {
        return CMM_DIR;
    }

    protected File iibDevelopmentDir() {
        return IIB_DEVELOPMENT_DIR;
    }

    protected File getEdmDevelopmentDir() {
        return EDM_DEVELOPMENT_DIR;
    }
}
