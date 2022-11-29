package soya.framework.action.dispatch;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import soya.framework.action.*;
import soya.framework.commons.util.URIUtils;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.*;

public class DispatchScheduler {
    private static final String DISPATCH_SCHEDULER = "DISPATCH-SCHEDULER";
    private static DispatchScheduler me;

    private Timer timer;
    private Map<String, DispatchTask> tasks = new HashMap<>();

    static {
        me = new DispatchScheduler();
    }

    private DispatchScheduler() {
        timer = new Timer("DISPATCH_SCHEDULER");
    }

    public String[] taskNames() {
        List<String> list = new ArrayList<>(tasks.keySet());
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

    public String getTaskDetails(String taskName) {
        if(tasks.containsKey(taskName)) {
            DispatchTask dispatchTask = tasks.get(taskName);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", dispatchTask.name);
            jsonObject.addProperty("dispatch", dispatchTask.dispatch);
            jsonObject.addProperty("period", dispatchTask.period);
            jsonObject.addProperty("delay", dispatchTask.delay);

            return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
        } else {
            return JsonNull.INSTANCE.toString();
        }
    }

    public void schedule(String name, String dispatch, long delay, long period) {
        if(!tasks.containsKey(name)) {
            DispatchTask task = new DispatchTask(name, dispatch, period, delay);
            timer.schedule(task, delay, period);
            tasks.put(name, task);
        }
    }

    public boolean cancel(String taskName) {
        DispatchTask task = tasks.get(taskName);
        boolean boo = task.cancel();
        tasks.remove(taskName);
        return boo;
    }


    public static DispatchScheduler getInstance() {
        return me;
    }

    static class DispatchTask extends TimerTask {

        private final String name;
        private final String dispatch;
        private final long period;
        private final long delay;

        DispatchTask(String name, String dispatch, long period, long delay) {
            this.name = name;
            this.dispatch = dispatch;
            this.period = Math.max(1000l, period);
            this.delay = Math.max(delay, 0);
        }

        @Override
        public void run() {
            try {
                URI uri = URI.create(dispatch);
                final ActionClass actionClass;
                if (uri.getScheme().equals("class")) {
                    try {
                        actionClass = ActionClass.get((Class<? extends ActionCallable>) Class.forName(uri.getHost()));
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e);
                    }
                } else {
                    actionClass = ActionContext.getInstance().getActionMappings().actionClass(ActionName.fromURI(uri));
                }

                if (actionClass == null) {
                    throw new IllegalArgumentException("Cannot find action class from uri: " + uri);
                }


                ActionExecutor executor = ActionExecutor.executor(actionClass.getActionType());
                URIUtils.splitQuery(uri.getQuery()).entrySet().forEach(e -> {
                    Field field = actionClass.getActionField(e.getKey());
                    if (field != null) {
                        String exp = e.getValue().get(0);
                        String value = exp;
                        if(exp.contains("(") && exp.endsWith(")")) {
                            Evaluation evaluation = new Evaluation(exp);
                            if(AssignmentType.VALUE.equals(evaluation.getAssignmentMethod())) {
                                value = evaluation.getExpression();

                            } else if(AssignmentType.RESOURCE.equals(evaluation.getAssignmentMethod())) {
                                value = Resources.getResourceAsString(evaluation.getExpression());
                            }
                        }

                        executor.setProperty(field.getName(), ConvertUtils.convert(value, field.getType()));
                    }

                });
                executor.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
