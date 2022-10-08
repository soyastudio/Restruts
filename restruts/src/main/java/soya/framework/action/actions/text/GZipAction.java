package soya.framework.action.actions.text;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

@ActionDefinition(domain = "text-util",
        name = "gzip",
        path = "/gzip",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "GZip",
        description = "GZip")
public class GZipAction extends TextUtilAction {

    @Override
    public String execute() throws Exception {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
                gzipOutputStream.write(text.getBytes(encoding));
            }

            byte[] encoded = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
            return new String(encoded);

        } catch (IOException e) {
            throw new RuntimeException("Failed to zip content", e);
        }
    }
}
