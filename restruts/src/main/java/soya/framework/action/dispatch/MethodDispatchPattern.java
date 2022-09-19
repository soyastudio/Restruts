package soya.framework.action.dispatch;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodDispatchPattern {

    Class<?> type();

    String methodName();

    Class<?>[] methodParameterTypes() default {};

    ActionPropertyAssignment[] propertyAssignments() default {};
}
