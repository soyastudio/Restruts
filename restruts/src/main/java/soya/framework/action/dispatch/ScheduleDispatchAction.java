package soya.framework.action.dispatch;

import soya.framework.action.*;
import soya.framework.action.dispatch.DispatchScheduler;

@ActionDefinition(domain = "dispatch",
        name = "schedule-dispatch-task",
        path = "/schedule/dispatch-task",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class ScheduleDispatchAction extends Action<Void> {

    @ActionProperty(
            description = {
                    "Task name"
            },
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            option = "n",
            displayOrder = 4
    )
    private String name;

    @ActionProperty(description = {
            "Action dispatch uri for identifying action with uri format 'domain://name' and assigning action property with query string format such as 'prop1=assign1(exp1)&prop2=assign2(exp2)'.",
            "Here assign() function should be one of val(exp), res(exp), param(exp) or ref(exp):",
            "- val(exp): directly assign property with string value from exp",
            "- res(exp): extract contents from resource uri exp, such as 'classpath://kafka-config.properties'",
            "- param(exp): evaluate value from payload input in json format using expression: exp",
            "- ref(exp): evaluate value from context using expression: exp, available for multiple action dispatch patterns such as pipeline, eventbus etc."
    },
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            option = "t",
            displayOrder = 5)
    private String dispatch;

    @ActionProperty(
            description = {
                    "Period in milliseconds."
            },
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            option = "p",
            displayOrder = 6)
    private long period;

    @ActionProperty(
            description = {
                    "Delay in milliseconds."
            },
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            option = "d",
            displayOrder = 7)
    private long delay;

    @Override
    public Void execute() throws Exception {
        DispatchScheduler.getInstance().schedule(name, dispatch, delay, period);
        return null;
    }
}
