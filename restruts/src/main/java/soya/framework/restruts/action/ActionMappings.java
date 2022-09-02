package soya.framework.restruts.action;

import java.lang.reflect.Field;
import java.util.Comparator;

public interface ActionMappings {

    String[] domains();

    Class<?> domainType(String domain);

    ActionName[] actions(String domain);

    Class<? extends ActionCallable> actionType(ActionName actionName);

    Field[] parameterFields(Class<? extends ActionCallable> actionType);

    final class ParameterFieldComparator implements Comparator<Field> {

        @Override
        public int compare(Field o1, Field o2) {
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
}
