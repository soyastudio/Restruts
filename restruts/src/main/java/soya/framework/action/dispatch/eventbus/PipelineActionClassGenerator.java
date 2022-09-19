package soya.framework.action.dispatch.eventbus;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;
import soya.framework.action.dispatch.DispatchClassGenerator;

@ActionDefinition(domain = "dispatch",
        name = "/generator-pipeline-action-class",
        path = "/dispatch/generator/pipeline-action-class",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Pipeline Action Class Generator",
        description = "Print as markdown format.")
public class PipelineActionClassGenerator extends DispatchClassGenerator {

}
