package soya.framework.action;

import java.io.Serializable;
import java.lang.reflect.Field;

public final class ActionProperty implements Comparable<ActionProperty>, Serializable {
    private final transient Field field;
    private final String name;

    ActionProperty(Field field) {
        this.field = field;
        this.name = field.getName();
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(ActionProperty o) {
        Field o1 = this.field;
        Field o2 = o.field;

        if (o1.getAnnotation(PayloadMapping.class) != null) {
            return 1;
        } else if (o2.getAnnotation(PayloadMapping.class) != null) {
            return -1;
        }

        if (o1.getAnnotation(ParameterMapping.class) != null && o2.getAnnotation(ParameterMapping.class) != null) {
            int result = ParameterMapping.ParameterType.index(o1.getAnnotation(ParameterMapping.class).parameterType())
                    - ParameterMapping.ParameterType.index(o2.getAnnotation(ParameterMapping.class).parameterType());
            if (result != 0) {
                return result;
            }
        }

        return o1.getName().compareTo(o2.getName());
    }
}
