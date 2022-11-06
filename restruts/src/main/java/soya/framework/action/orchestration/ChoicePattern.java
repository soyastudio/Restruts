package soya.framework.action.orchestration;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChoicePattern {

    When[] whens();

    String otherwise();

}
