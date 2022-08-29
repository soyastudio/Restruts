package soya.framework.restruts.actions.albertsons.transform;

import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

@OperationMapping(domain = "albertsons",
        name = "gzip",
        path = "/workshop/transform/gzip",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class GZipAction extends Converter {

    @Override
    public String execute() throws Exception {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
                gzipOutputStream.write(message.getBytes(StandardCharsets.UTF_8));
            }

            byte[] compressed = byteArrayOutputStream.toByteArray();

            return Base64.getEncoder().encodeToString(compressed);

        } catch (IOException e) {
            throw e;
        }
    }
}