package soya.framework.io.resources;

import soya.framework.io.Resource;
import soya.framework.io.ResourceException;
import soya.framework.lang.Named;

import java.io.InputStream;
import java.net.URI;

@Named("classpath")
public class ClassPathResource implements Resource {

    @Override
    public InputStream getAsInputStream(URI uri) throws ResourceException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassPathResource.class.getClassLoader();
        }

        String res = uri.getPath() != null ? uri.getHost() + uri.getPath() : uri.getHost();
        return classLoader.getResourceAsStream(res);
    }
}
