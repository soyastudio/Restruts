package soya.framework.io.resources;

import soya.framework.io.Resource;
import soya.framework.io.ResourceException;
import soya.framework.io.ResourceService;
import soya.framework.lang.Named;
import soya.framework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

@Named("gzip")
public class GZipEncodedResource implements Resource {

    @Override
    public InputStream getAsInputStream(URI uri) throws ResourceException {
        if(uri.getHost() != null) {
            return unzip(uri.getHost().getBytes(StandardCharsets.UTF_8));
        } else {
            URI sub = URI.create(uri.getSchemeSpecificPart());
            if(sub.getScheme() != null && !sub.getScheme().equals("gzip")) {
                try {
                    return unzip(StreamUtils.copyToByteArray(ResourceService.getAsInputStream(sub)));
                } catch (IOException e) {
                    throw new ResourceException(e);
                }
            }
        }

        throw new ResourceException(uri);
    }

    private InputStream unzip(byte[] data) {
        byte[] compressed = Base64.getDecoder().decode(data);
        try {
            return new GZIPInputStream(new ByteArrayInputStream(compressed));
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }
}
