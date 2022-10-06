package soya.framework.restruts.pattern;

import soya.framework.action.dispatch.*;

@ActionProxyPattern
public interface Workshop {
    @ActionDispatchPattern(uri = "about://about")
    String about();

    @ActionDispatchPattern(uri = "albertsons://base64-encode",
            propertyAssignments = {
                    @ActionPropertyAssignment(name = "message", assignmentMethod = AssignmentMethod.PARAMETER, expression = "msg")
            })
    String base64Encode(@ParamName("msg") String msg);

    @ActionDispatchPattern(uri = "albertsons://base64-decode",
            propertyAssignments = {
                    @ActionPropertyAssignment(name = "message", assignmentMethod = AssignmentMethod.PARAMETER, expression = "msg")
            })
    String base64Decode(@ParamName("msg") String msg);

}
