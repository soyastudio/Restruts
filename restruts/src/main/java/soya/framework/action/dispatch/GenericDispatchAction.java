package soya.framework.action.dispatch;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import soya.framework.action.*;

import java.io.IOException;

public abstract class GenericDispatchAction<T> extends Action<T> {

    @ActionProperty(description = {
            "Data input based on dispatch settings above:",
            "- If parameter number is one and parameter type is simple type, parameter value is simple string;",
            "- If parameter number is larger than one, value should be in json object with parameter name as key and json element as value."
    },
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            contentType = MediaType.TEXT_PLAIN,
            required = true,
            option = "p")
    protected String data;

    protected Object evaluate(Assignment assignment, Class<?> type, Object context) {
        Object value = null;
        if (AssignmentMethod.VALUE.equals(assignment.getAssignmentMethod())) {
            value = ConvertUtils.convert(assignment.getExpression(), type);

        } else if (AssignmentMethod.RESOURCE.equals(assignment.getAssignmentMethod())) {
            try {
                value = ConvertUtils.convert(Resources.getResourceAsString(assignment.getExpression()), type);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        } else if (AssignmentMethod.REFERENCE.equals(assignment.getAssignmentMethod())) {
            value = ActionContext.getInstance().getService(type);

        } else if (AssignmentMethod.PARAMETER.equals(assignment.getAssignmentMethod())) {
            if (context instanceof JsonElement) {
                JsonObject jsonObject = ((JsonElement) context).getAsJsonObject();
                value = ConvertUtils.convert(jsonObject.get(assignment.getExpression()), type);
            } else {
                value = ConvertUtils.convert(context, type);
            }
        }

        return null;
    }

}
