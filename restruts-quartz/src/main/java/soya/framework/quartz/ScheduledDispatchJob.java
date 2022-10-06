package soya.framework.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import soya.framework.action.ActionContext;
import soya.framework.action.ConvertUtils;
import soya.framework.action.dispatch.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ScheduledDispatchJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        if (getClass().getAnnotation(ActionDispatchPattern.class) != null) {
            ActionDispatch actionDispatch = ActionDispatch.fromAnnotation(getClass().getAnnotation(ActionDispatchPattern.class));
            actionDispatch(actionDispatch, jobExecutionContext);

        } else if (getClass().getAnnotation(CommandDispatchPattern.class) != null) {

        } else if (getClass().getAnnotation(MethodDispatchPattern.class) != null) {
            MethodDispatchPattern methodDispatchPattern = getClass().getAnnotation(MethodDispatchPattern.class);
            Class<?> clazz = methodDispatchPattern.type();
            MethodParameterAssignment[] assignments = methodDispatchPattern.parameterAssignments();
            Class<?>[] paramTypes = new Class[assignments.length];
            Object[] paramValues = new Object[assignments.length];
            for (int i = 0; i < assignments.length; i++) {
                paramTypes[i] = assignments[i].type();
                if (assignments[i].assignmentMethod().equals(AssignmentMethod.VALUE)) {
                    paramValues[i] = ConvertUtils.convert(assignments[i].expression(), paramTypes[i]);
                }
            }

            try {
                Method method = clazz.getMethod(methodDispatchPattern.methodName(), paramTypes);
                Object impl = Modifier.isStatic(method.getModifiers()) ? null : ActionContext.getInstance().getService(clazz);
                method.invoke(impl, paramValues);

            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new JobExecutionException(e);
            }

        }
    }

    protected void actionDispatch(ActionDispatch actionDispatch, JobExecutionContext jobExecutionContext) {

    }
}
