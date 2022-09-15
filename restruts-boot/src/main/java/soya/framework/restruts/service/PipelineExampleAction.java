package soya.framework.restruts.service;

import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.dispatch.Pipeline;
import soya.framework.action.dispatch.PipelineAction;
import soya.framework.action.dispatch.Task;

@ActionDefinition(domain = "pattern",
        name = "pipeline-example",
        path = "/pattern/pipeline/example",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
@Pipeline(tasks = {
        @Task(name = "encode", dispatch = "")
})
public class PipelineExampleAction extends PipelineAction<String> {

}
