package soya.framework.io.fragments;

import soya.framework.annotation.Named;
import soya.framework.io.ResourceException;
import soya.framework.io.ResourceFilter;

@Named("process")
public class DispatchFilter implements ResourceFilter {
    private ResourceFilter processor;

    public DispatchFilter(String className) {
        try {
            processor = (ResourceFilter) Class.forName(className).newInstance();

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public byte[] process(byte[] data) throws ResourceException {
        return processor.process(data);
    }
}
