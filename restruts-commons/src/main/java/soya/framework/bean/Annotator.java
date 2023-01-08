package soya.framework.bean;

public interface Annotator<T extends Annotatable> {
    void annotate(T annotatable);

}
