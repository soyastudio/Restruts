package soya.framework.action.dispatch.fragments;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;

@FragmentFunction("mustache")
public class MustacheProcessor extends AbstractFragmentProcessor<String> {

    public MustacheProcessor(String[] exp) {
        super(exp);
    }

    @Override
    public String process(ActionResult in) {
        return null;
    }
}
