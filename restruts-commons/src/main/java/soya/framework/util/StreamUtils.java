package soya.framework.util;

import java.io.*;
import java.nio.charset.Charset;

public class StreamUtils {

    public static final int BUFFER_SIZE = 4096;
    private static final byte[] EMPTY_CONTENT = new byte[0];

    public StreamUtils() {
    }

    public static String read(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        byte[] bin = copyToByteArray(inputStream);
        inputStream.close();
        return new String(bin, Charset.defaultCharset());
    }

    public static void write(String contents, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(contents);
        writer.flush();
        writer.close();
    }

    public static byte[] copyToByteArray(InputStream in) throws IOException {
        if (in == null) {
            return new byte[0];
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            copy((InputStream)in, out);
            return out.toByteArray();
        }
    }

    public static int copy(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;

        int bytesRead;
        for(byte[] buffer = new byte[4096]; (bytesRead = in.read(buffer)) != -1; byteCount += bytesRead) {
            out.write(buffer, 0, bytesRead);
        }

        out.flush();
        return byteCount;
    }
}
