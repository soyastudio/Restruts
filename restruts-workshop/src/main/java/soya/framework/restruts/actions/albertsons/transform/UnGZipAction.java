package soya.framework.restruts.actions.albertsons.transform;

import soya.framework.commons.util.StreamUtils;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

@OperationMapping(domain = "albertsons",
        name = "ungzip",
        path = "/workshop/transform/ungzip",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class UnGZipAction extends Converter {

    @Override
    public String execute() throws Exception {
        byte[] decoded = Base64.getDecoder().decode(message);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] decompressed = StreamUtils.copyToByteArray((new GZIPInputStream(new ByteArrayInputStream(decoded))));
            return new String(decompressed);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
