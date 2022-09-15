package soya.framework.action;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public interface Resource {

    String getAsString(Charset encoding) throws IOException;

    InputStream getAsInputStream() throws IOException;


}
