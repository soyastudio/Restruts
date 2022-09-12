package soya.framework.kafka.actions;

import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;

@OperationMapping(domain = "kafka",
        name = "consume-seek-to-end",
        path = "/kafka/consume/seek-to-end",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class ConsumeSeekToEndAction extends ConsumeAction<String> {

    @Override
    public String execute() throws Exception {
        return null;
    }
}
