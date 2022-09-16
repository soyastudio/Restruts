package soya.framework.restruts.service;

import soya.framework.action.dispatch.*;

@ActionProxy
public interface Workshop {

    @ActionMapping(uri = "about://about")
    String about();

    @ActionMapping(uri = "albertsons://base64-encode",
            parameters = {
                    @ActionParameter(name = "message", assignmentMethod = AssignmentMethod.VALUE, expression = "Once upon a time...")
            })
    String base64Encode();

    @ActionMapping(uri = "albertsons://base64-decode",
            parameters = {
                    @ActionParameter(name = "message", assignmentMethod = AssignmentMethod.PARAMETER, expression = "msg")
            })
    String base64Decode(@ParamName("msg") String msg);

}
