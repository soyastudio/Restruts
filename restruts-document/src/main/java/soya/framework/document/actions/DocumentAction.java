package soya.framework.document.actions;

import soya.framework.action.Action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;

public abstract class DocumentAction<T> extends Action<T> {

    protected InputStream getResourceAsInputStream(String uri) throws IOException {
        return URI.create(uri).toURL().openStream();
    }

    protected File getFile(String uri) throws MalformedURLException {
        return new File(URI.create(uri).toURL().getFile());
    }
}
