package soya.framework.bean;

public interface DynaClass extends Annotatable{
    String getName();

    DynaProperty getDynaProperty(String name);

    DynaProperty[] getDynaProperties();

    DynaBean newInstance()
            throws IllegalAccessException, InstantiationException;
}
