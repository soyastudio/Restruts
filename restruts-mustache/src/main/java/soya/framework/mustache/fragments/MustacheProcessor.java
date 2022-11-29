package soya.framework.mustache.fragments;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;
import soya.framework.action.dispatch.fragments.AbstractProcessor;

@FragmentFunction("mustache")
public class MustacheProcessor extends AbstractProcessor<String> {

    private String uri;

    public MustacheProcessor(String[] exp) {
        super(exp);
        this.uri = exp[0];
    }

    @Override
    public String process(ActionResult in) {
        return null;
    }
}
