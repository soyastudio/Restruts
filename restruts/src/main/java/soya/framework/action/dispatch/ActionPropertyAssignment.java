package soya.framework.action.dispatch;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionPropertyAssignment {
    String name();

    AssignmentMethod assignmentMethod();

    String expression();

}
