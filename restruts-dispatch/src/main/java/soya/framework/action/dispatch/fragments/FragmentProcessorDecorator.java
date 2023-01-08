package soya.framework.action.dispatch.fragments;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;
import soya.framework.action.dispatch.FragmentProcessor;

@FragmentFunction("process")
public class FragmentProcessorDecorator extends AbstractFragmentProcessor {

    private FragmentProcessor fragmentProcessor;

    public FragmentProcessorDecorator(String[] exp) {
        super(exp);
        try {
            String className = exp[0];
            String[] args = new String[exp.length - 1];

            if(args.length > 0) {
                System.arraycopy(exp, 1, args, 0, args.length);
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

    }

    @Override
    public Object process(ActionResult in) {
        return fragmentProcessor.process(in);
    }
}
