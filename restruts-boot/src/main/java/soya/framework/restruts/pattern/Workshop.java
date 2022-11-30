package soya.framework.restruts.pattern;

import soya.framework.action.dispatch.ActionDispatchPattern;
import soya.framework.action.dispatch.ActionPropertyAssignment;
import soya.framework.action.dispatch.AssignmentType;
import soya.framework.action.dispatch.ParamName;
import soya.framework.action.dispatch.proxy.ActionProxyPattern;

@ActionProxyPattern
public interface Workshop {
    @ActionDispatchPattern(uri = "about://about")
    String about();

    @ActionDispatchPattern(
            uri = "albertsons://base64-encode?message=param(msg)"
    )
    String base64Encode(
            @ParamName("msg") String msg
    );

    @ActionDispatchPattern(
            uri = "albertsons://base64-decode?message=param(msg)#base64encode()"
    )
    String base64Decode(@ParamName("msg") String msg);

}
