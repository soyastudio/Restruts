package soya.framework.action.dispatch.fragments;

public abstract class Base64Processor extends AbstractProcessor<String> {

    protected String encoding = "utf-8";

    public Base64Processor(String[] exp) {
        super(exp);
        if(exp.length == 1) {
            encoding = exp[0];
        }
    }
}
