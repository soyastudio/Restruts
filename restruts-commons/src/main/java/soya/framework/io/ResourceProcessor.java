package soya.framework.io;

import soya.framework.lang.FunctionProcessor;

public interface ResourceProcessor extends FunctionProcessor<byte[]> {
    byte[] process(byte[] data) throws ResourceException;
}
