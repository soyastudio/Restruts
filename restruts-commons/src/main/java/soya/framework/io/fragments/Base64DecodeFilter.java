package soya.framework.io.fragments;

import soya.framework.annotation.Named;
import soya.framework.io.ResourceFilter;
import soya.framework.util.Base64Utils;

@Named("base64decode")
public class Base64DecodeFilter implements ResourceFilter {

    @Override
    public byte[] process(byte[] data) {
        return Base64Utils.decode(data);
    }
}
