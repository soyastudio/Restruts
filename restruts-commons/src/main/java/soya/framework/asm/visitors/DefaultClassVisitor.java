package soya.framework.asm.visitors;

import org.objectweb.asm.*;

public class DefaultClassVisitor extends ClassVisitor {

    public DefaultClassVisitor() {
        super(Opcodes.ASM4);
    }

    public DefaultClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM4, classVisitor);
    }

    @Override
    public void visitSource(String s, String s1) {
        System.out.println("============================ S: " + s);
        System.out.println("============================ S1: " + s1);
        super.visitSource(s, s1);
    }
/*

    @Override
    public ModuleVisitor visitModule() {
        return super.visitModule();
    }
*/

    @Override
    public void visitOuterClass(String s, String s1, String s2) {
        super.visitOuterClass(s, s1, s2);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        return super.visitAnnotation(s, b);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String s, boolean b) {
        return super.visitTypeAnnotation(i, typePath, s, b);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
    }

    @Override
    public void visitInnerClass(String s, String s1, String s2, int i) {
        super.visitInnerClass(s, s1, s2, i);
    }

    @Override
    public FieldVisitor visitField(int i, String s, String s1, String s2, Object o) {
        return super.visitField(i, s, s1, s2, o);
    }

    @Override
    public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
        return super.visitMethod(i, s, s1, s2, strings);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
