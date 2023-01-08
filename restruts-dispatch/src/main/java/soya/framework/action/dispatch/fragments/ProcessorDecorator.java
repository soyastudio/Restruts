package soya.framework.action.dispatch.fragments;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;
import soya.framework.action.dispatch.FragmentProcessor;

@FragmentFunction("process")
public class ProcessorDecorator extends AbstractProcessor {

    private FragmentProcessor processor;

    public ProcessorDecorator(String[] exp) {
        super(exp);
        try {
            String className = exp[0];

            String[] args = new String[exp.length - 1];
            if (args.length > 0) {
                System.arraycopy(exp, 1, args, 0, args.length);
            }

            this.processor = (FragmentProcessor) Class.forName(className)
                    .getConstructor(new Class[]{String[].class})
                    .newInstance(new Object[]{args});

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Object process(ActionResult in) {
        return processor.process(in);
    }
}
