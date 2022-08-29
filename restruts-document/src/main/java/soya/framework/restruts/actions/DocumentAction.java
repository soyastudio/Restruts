package soya.framework.restruts.actions;

import soya.framework.restruts.action.Action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;

public abstract class DocumentAction<T> extends Action<T> {

    public static final String SCHEMA_SYSTEM = "system";

    public static final String SCHEMA_BASE64 = "base64";

    public static final String SCHEMA_GZIP = "gzip";

    public static final String SCHEMA_CLASSPATH = "classpath";

    public static final String SCHEMA_HOME = "home";

    public static final String SCHEMA_USER_HOME = "user-home";

    protected InputStream getResourceAsInputStream(String uri) throws IOException {
        return URI.create(uri).toURL().openStream();
    }

    protected File getFile(String uri) throws MalformedURLException {
        return new File(URI.create(uri).toURL().getFile());
    }
}
