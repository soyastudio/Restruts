package soya.framework.action.actions.reflect;

import org.reflections.Reflections;
import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ActionDefinition(domain = "reflect",
        name = "discovery",
        path = "/discovery",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "About",
        description = "Print as markdown format.")
public class TypeDiscoveryAction extends Action<String[]> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true, option = "t")
    private String type;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true, option = "p")
    protected String packageName;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, option = "a", displayOrder = 6)
    protected boolean includeAbstract;

    @Override
    public String[] execute() throws Exception {

        List<String> list = new ArrayList<>();
        Class<?> cls = Class.forName(type);

        Reflections reflections = packageName == null? new Reflections() : new Reflections(packageName);
        if(cls.isAnnotation()) {
            reflections.getTypesAnnotatedWith((Class<? extends Annotation>) cls).forEach(e -> {
                if(!Modifier.isAbstract(e.getModifiers()) || includeAbstract) {
                    list.add(e.getName());
                }
            });

        } else {
            reflections.getSubTypesOf(cls).forEach(e -> {
                if(!Modifier.isAbstract(e.getModifiers()) || includeAbstract) {
                    list.add(e.getName());
                }
            });
        }
        Collections.sort(list);

        return list.toArray(new String[list.size()]);
    }
}
