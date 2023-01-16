package soya.framework.io;

import soya.framework.pattern.FunctionalFilter;

public interface ResourceFilter extends FunctionalFilter<byte[]> {
    byte[] process(byte[] data) throws ResourceException;
}
