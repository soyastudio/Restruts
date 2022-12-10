package soya.framework.action.actions.reflect;

import org.reflections.Reflections;
import soya.framework.action.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ActionDefinition(
        domain = "reflect",
        name = "type-scan",
        path = "/util/type-scan",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Discovery Runtime Types",
        description = "Scan classpath for specified types."
)
public class TypeScanAction extends Action<String[]> {

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            option = "t",
            description = "Class name for search."
    )
    private String type;

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            option = "p",
            description = "Package name under which to search."
    )
    protected String packageName;

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            option = "a",
            displayOrder = 6,
            description = "If abstract classes are included."
    )
    protected boolean includeAbstract;

    @Override
    public String[] execute() throws Exception {

        List<String> list = new ArrayList<>();
        Class<?> cls = Class.forName(type);

        Reflections reflections = packageName == null ? new Reflections() : new Reflections(packageName);
        if (cls.isAnnotation()) {
            reflections.getTypesAnnotatedWith((Class<? extends Annotation>) cls).forEach(e -> {
                if (!Modifier.isAbstract(e.getModifiers()) || includeAbstract) {
                    list.add(e.getName());
                }
            });

        } else {
            reflections.getSubTypesOf(cls).forEach(e -> {
                if (!Modifier.isAbstract(e.getModifiers()) || includeAbstract) {
                    list.add(e.getName());
                }
            });
        }
        Collections.sort(list);

        return list.toArray(new String[list.size()]);
    }
}
