package soya.framework.action.actions.reflect;

import com.google.gson.GsonBuilder;
import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ActionDefinition(
        domain = "reflect",
        name = "runtime-threads",
        path = "/runtime/threads",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Threads",
        description = "List runtime threads."
)
public class RuntimeThreadsAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        List<ThreadModel> list = new ArrayList<>();
        Thread.getAllStackTraces().keySet().forEach(e -> {
            list.add(new ThreadModel(e));
        });

        Collections.sort(list);
        return new GsonBuilder().setPrettyPrinting().create().toJson(list);
    }

    static class ThreadModel implements Comparable<ThreadModel> {
        private String name;
        private String type;

        private ThreadModel(Thread thread) {
            this.name = thread.getName();
            this.type = thread.getClass().getName();
        }

        @Override
        public int compareTo(ThreadModel o) {
            return this.name.compareTo(o.name);
        }
    }
}
