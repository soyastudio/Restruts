package soya.framework.restruts.pattern;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.orchestration.pipeline.AnnotatedPipelineAction;
import soya.framework.action.orchestration.pipeline.PipelinePattern;
import soya.framework.action.orchestration.pipeline.TaskDefinition;

@ActionDefinition(domain = "pattern",
        name = "pipeline",
        path = "/pattern/pipeline",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
@PipelinePattern(
        tasks = {
                @TaskDefinition(name = "extract", dispatch = "class://soya.framework.action.actions.reflect.EchoAction?message=param(message)"),
                @TaskDefinition(name = "transform", dispatch = "text-util://base64-encode?text=ref(extract)"),
                @TaskDefinition(name = "load", dispatch = "text-util://base64-decode?text=ref(transform)")
        }
)
public class PipelinePatternAction extends AnnotatedPipelineAction<String> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.PAYLOAD)
    private String message;
}
