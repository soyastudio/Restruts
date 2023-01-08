package soya.framework.io.fragments;

import soya.framework.io.ResourceProcessor;
import soya.framework.lang.Named;
import soya.framework.util.Base64Utils;

@Named("base64decode")
public class Base64DecodeProcessor implements ResourceProcessor {

    @Override
    public byte[] process(byte[] data) {
        return Base64Utils.decode(data);
    }
}
