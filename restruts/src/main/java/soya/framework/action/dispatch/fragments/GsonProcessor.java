package soya.framework.action.dispatch.fragments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.FragmentFunction;

@FragmentFunction("gson")
public class GsonProcessor extends AbstractProcessor<String> {
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public GsonProcessor(String[] exp) {
        super(exp);
    }

    @Override
    public String process(ActionResult in) {
        Object o = in.get();
        return GSON.toJson(o);
    }
}
