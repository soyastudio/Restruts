package soya.framework.action.dispatch.fragments;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@FragmentFunction("base64encode")
public class Base64EncodeProcessor extends Base64Processor {

    public Base64EncodeProcessor(String[] exp) {
        super(exp);
    }

    @Override
    public String process(ActionResult in) {
        try {
            return Base64.getEncoder().encodeToString(in.get().toString().getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
