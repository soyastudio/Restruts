package soya.framework.action.dispatch.fragments;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@FragmentFunction("base64decode")
public class Base64DecodeProcessor extends Base64Processor {

    public Base64DecodeProcessor(String[] exp) {
        super(exp);
    }

    @Override
    public String process(ActionResult in) {
        try {
            return new String(Base64.getDecoder().decode(in.get().toString()), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
