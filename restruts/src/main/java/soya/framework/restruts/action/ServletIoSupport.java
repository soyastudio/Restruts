package soya.framework.restruts.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ServletIoSupport {

    private static DefaultObjectReader defaultObjectReader = new DefaultObjectReader();
    private static DefaultObjectWriter defaultObjectWriter = new DefaultObjectWriter();

    private ServletIoSupport() {
    }

    public static void write(Object o, String contentType, OutputStream outputStream) throws IOException {
        getWriter(o, contentType).write(o, contentType, outputStream);

    }

    private static ObjectWriter getWriter(Object o, String contentType) {
        return defaultObjectWriter;
    }

    static class DefaultObjectReader implements ObjectReader {

        @Override
        public <T> T read(InputStream inputStream, String contentType, Class<T> type) throws IOException {
            return null;
        }
    }

    static class DefaultObjectWriter implements ObjectWriter {

        @Override
        public void write(Object object, String contentType, OutputStream outputStream) throws IOException {
            if(MediaType.TEXT_PLAIN.equals(contentType)) {
                outputStream.write(object.toString().getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
