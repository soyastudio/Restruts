package soya.framework.action.dispatch.fragments;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;

@FragmentFunction("xpath")
public class XPathProcessor extends AbstractProcessor<String> {

    public XPathProcessor(String[] exp) {
        super(exp);
    }

    @Override
    public String process(ActionResult in) {
        return null;
    }
}
