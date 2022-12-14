package soya.framework.action.orchestration.choice;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChoicePattern {

    When[] whens();

    String otherwise();

}
