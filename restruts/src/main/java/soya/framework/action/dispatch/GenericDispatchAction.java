package soya.framework.action.dispatch;

import com.google.gson.*;
import soya.framework.action.Action;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

public abstract class GenericDispatchAction<T> extends Action<T> {
    public static Evaluator evaluator = new DefaultEvaluator();

    @ActionProperty(description = {
            "Data input based on dispatch settings above:",
            "- If parameter number is one and parameter type is simple type, parameter value is simple string;",
            "- If parameter number is larger than one, value should be in json object with parameter name as key and json element as value."
    },
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            contentType = MediaType.TEXT_PLAIN,
            option = "p")
    protected String data;

    protected JsonElement jsonElement() {
        if(data == null) {
            return JsonNull.INSTANCE;
        } else {
            try {
                return JsonParser.parseString(data);

            } catch (JsonSyntaxException e) {
                return new JsonPrimitive(data);
            }
        }
    }

    protected Object evaluate(Assignment assignment, Class<?> type, Object context) {
        return evaluator.evaluate(assignment, context, type);
    }

}
