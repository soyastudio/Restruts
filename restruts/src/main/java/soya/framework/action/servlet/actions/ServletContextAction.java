package soya.framework.action.servlet.actions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import soya.framework.action.Action;
import soya.framework.action.WiredService;

import javax.servlet.ServletContext;

public abstract class ServletContextAction<T> extends Action<T> {
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @WiredService
    protected ServletContext servletContext;
}
