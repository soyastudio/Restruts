package soya.framework.xmlbeans;

import soya.framework.action.Action;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;

public abstract class XmlBeansAction<T> extends Action<T> {

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM)
    protected String schemaURI;

    protected File getFile(String uri) throws MalformedURLException {
        return new File(URI.create(uri).toURL().getFile());
    }
}
