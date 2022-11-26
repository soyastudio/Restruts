package soya.framework.action.dispatch.fragments;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

@FragmentFunction("gzip")
public class GZipProcessor extends Base64Processor {

    public GZipProcessor(String[] exp) {
        super(exp);
    }

    @Override
    public String process(ActionResult in) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
                gzipOutputStream.write(in.get().toString().getBytes(encoding));
            }

            byte[] encoded = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
            return new String(encoded);

        } catch (IOException e) {
            throw new RuntimeException("Failed to zip content", e);
        }
    }
}
