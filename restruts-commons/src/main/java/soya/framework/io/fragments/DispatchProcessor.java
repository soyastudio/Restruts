package soya.framework.io.fragments;

import soya.framework.io.ResourceException;
import soya.framework.io.ResourceProcessor;
import soya.framework.lang.Named;

@Named("process")
public class DispatchProcessor implements ResourceProcessor {
    private ResourceProcessor processor;

    public DispatchProcessor(String className) {
        try {
            processor = (ResourceProcessor) Class.forName(className).newInstance();

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public byte[] process(byte[] data) throws ResourceException {
        return processor.process(data);
    }
}
