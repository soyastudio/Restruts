package soya.framework.action.dispatch.evaluators;

import com.google.gson.JsonElement;
import soya.framework.action.dispatch.Evaluator;

public class JsonPayloadEvaluator implements Evaluator<JsonElement> {
    @Override
    public Object evaluate(String expression, JsonElement context) {
        if(context.isJsonObject()) {
            return context.getAsJsonObject().get(expression).getAsString();

        } else if(context.isJsonArray()) {
            return context.getAsJsonArray().get(Integer.parseInt(expression));

        }

        return null;
    }
}
