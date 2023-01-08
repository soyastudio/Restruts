package soya.framework.io.fragments;

import soya.framework.io.ResourceProcessor;
import soya.framework.lang.Named;

import java.util.Arrays;

@Named("jsonpath")
public class JsonPathProcessor implements ResourceProcessor {
    private String[] args;
    private String par;

    public JsonPathProcessor(String[] args, String par) {
        this.args = args;
        this.par = par;
    }

    @Override
    public byte[] process(byte[] data) {
        Arrays.stream(args).forEach(e -> {
            System.out.println(e);
        });

        return new byte[0];
    }
}
