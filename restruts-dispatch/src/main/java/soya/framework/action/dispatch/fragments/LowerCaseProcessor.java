package soya.framework.action.dispatch.fragments;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;

@FragmentFunction("lowercase")
public class LowerCaseProcessor extends AbstractProcessor<String> {

    public LowerCaseProcessor(String[] exp) {
        super(exp);
    }

    @Override
    public String process(ActionResult in) {
        return in.get().toString().toLowerCase();
    }
}
