package soya.framework.action.dispatch.pattern;

import soya.framework.action.dispatch.*;

@ActionProxyPattern
public interface Workshop {
    @ActionDispatchPattern(uri = "about://about")
    String about();

    @ActionDispatchPattern(uri = "albertsons://base64-encode",
            propertyAssignments = {
                    @ActionPropertyAssignment(name = "message", assignmentMethod = AssignmentMethod.VALUE, expression = "Once upon a time...")
            })
    String base64Encode();

    @ActionDispatchPattern(uri = "albertsons://base64-decode",
            propertyAssignments = {
                    @ActionPropertyAssignment(name = "message", assignmentMethod = AssignmentMethod.PARAMETER, expression = "msg")
            })
    String base64Decode(@ParamName("msg") String msg);

}
