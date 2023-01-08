package soya.framework.io;

import java.io.InputStream;
import java.net.URI;

public interface Resource {
    InputStream getAsInputStream(URI uri) throws ResourceException;
}
