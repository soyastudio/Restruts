package soya.framework.restruts.service;

import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.patterns.Pipeline;
import soya.framework.action.patterns.PipelineAction;
import soya.framework.action.patterns.Task;

@ActionDefinition(domain = "pattern",
        name = "pipeline-example",
        path = "/pattern/pipeline/example",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
@Pipeline(tasks = {
        @Task(name = "encode", signature = "")
})
public class PipelineExampleAction extends PipelineAction<String> {

}
