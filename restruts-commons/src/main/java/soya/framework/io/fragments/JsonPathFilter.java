package soya.framework.io.fragments;

import soya.framework.annotation.Named;
import soya.framework.io.ResourceFilter;

import java.util.Arrays;

@Named("jsonpath")
public class JsonPathFilter implements ResourceFilter {
    private String[] args;
    private String par;

    public JsonPathFilter(String[] args, String par) {
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
