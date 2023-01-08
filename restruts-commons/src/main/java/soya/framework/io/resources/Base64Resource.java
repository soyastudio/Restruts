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

@Named("base64")
public class Base64Resource implements Resource {

    @Override
    public InputStream getAsInputStream(URI uri) throws ResourceException {
        if (uri.getHost() != null) {
            return new ByteArrayInputStream(Base64.getDecoder().decode(uri.getHost().getBytes(StandardCharsets.UTF_8)));

        } else {
            URI sub = URI.create(uri.getSchemeSpecificPart());
            if (sub.getScheme() != null && !sub.getScheme().equalsIgnoreCase("base64")) {
                InputStream is = ResourceService.getAsInputStream(sub);
                try {
                    return new ByteArrayInputStream(Base64.getDecoder().decode(StreamUtils.copyToByteArray(is)));

                } catch (IOException e) {
                    throw new ResourceException(e);
                }
            }

            throw new ResourceException(uri);
        }
    }

}
