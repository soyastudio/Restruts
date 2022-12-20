package soya.framework.action.orchestration.choice;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.orchestration.ProcessException;
import soya.framework.action.orchestration.ProcessSession;

@ActionDefinition(domain = "pattern",
        name = "choice",
        path = "/pattern/orchestration/choice",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class ChoiceActionExample extends ChoiceAction {

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM, required = true)
    private Integer number;

    @Override
    protected void buildChoice(Choice.Builder builder) {
        builder
                .when(new Condition() {
                    @Override
                    public Boolean execute(ProcessSession session) throws ProcessException {
                        return number % 3 == 0;
                    }
                }, "reflect://util-echo?message=val(yes)")
                .otherwise("reflect://util-echo?message=val(no)");
    }
}
