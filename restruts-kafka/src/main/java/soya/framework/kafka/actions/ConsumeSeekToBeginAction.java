package soya.framework.kafka.actions;

import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;

@OperationMapping(domain = "kafka",
        name = "consume-seek-to-begin",
        path = "/kafka/consume/seek-to-begin",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class ConsumeSeekToBeginAction extends ConsumeAction<String>{

    @Override
    public String execute() throws Exception {
        return null;
    }
}
