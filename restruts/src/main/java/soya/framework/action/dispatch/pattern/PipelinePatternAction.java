package soya.framework.action.dispatch.pattern;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;
import soya.framework.action.dispatch.eventbus.PipelineAction;
import soya.framework.action.dispatch.eventbus.PipelinePattern;

@ActionDefinition(domain = "pattern",
        name = "pipeline",
        path = "/pattern/pipeline",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
@PipelinePattern(tasks = {
})
public class PipelinePatternAction extends PipelineAction<String> {

}
