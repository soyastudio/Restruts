package soya.framework.io;

import java.net.URI;

public class ResourceException extends RuntimeException {

    public ResourceException() {
    }

    public ResourceException(URI uri) {
        super("Cannot get data from resource: " + uri);
    }

    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceException(Throwable cause) {
        super(cause);
    }
}