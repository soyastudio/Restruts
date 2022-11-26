package soya.framework.action.dispatch.fragments;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;

@FragmentFunction("uppercase")
public class UpperCaseProcessor extends AbstractFragmentProcessor<String> {

    public UpperCaseProcessor(String[] exp) {
        super(exp);
    }

    @Override
    public String process(ActionResult in) {
        return null;
    }
}
