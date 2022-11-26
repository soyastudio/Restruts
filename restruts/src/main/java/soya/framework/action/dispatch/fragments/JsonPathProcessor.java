package soya.framework.action.dispatch.fragments;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;

@FragmentFunction("jsonpath")
public class JsonPathProcessor extends AbstractFragmentProcessor<String> {

    public JsonPathProcessor(String[] exp) {
        super(exp);
    }

    @Override
    public String process(ActionResult in) {
        return null;
    }
}
