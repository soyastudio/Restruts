package soya.framework.action.dispatch.fragments;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;

import java.io.*;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

@FragmentFunction("unzip")
public class UnzipProcessor extends Base64Processor {

    public UnzipProcessor(String[] exp) {
        super(exp);
    }

    @Override
    public String process(ActionResult in) {
        byte[] encoded = new byte[0];
        try {
            encoded = in.get().toString().getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }

        byte[] compressed = Base64.getDecoder().decode(encoded);

        if ((compressed == null) || (compressed.length == 0)) {
            throw new IllegalArgumentException("Cannot unzip null or empty bytes");
        }
        if (!isZipped(compressed)) {
            return new String(compressed);
        }

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressed)) {
            try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream, encoding)) {
                    try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                        StringBuilder output = new StringBuilder();
                        String line;
                        boolean boo = false;
                        while ((line = bufferedReader.readLine()) != null) {
                            if (boo) {
                                output.append("\n");
                            } else {
                                boo = true;
                            }

                            output.append(line);
                        }

                        return output.toString();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to unzip content", e);
        }
    }

    private boolean isZipped(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC))
                && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }
}
