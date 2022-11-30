package soya.framework.action.dispatch.proxy;

import soya.framework.action.Action;
import soya.framework.action.dispatch.AssignmentType;

import java.util.ArrayList;
import java.util.List;

public abstract class ProxyInterfaceGenerator extends Action<String> {




    protected static class ProxyInterface {
        private String packageName;
        private String className;

    }

    protected static class ProxyMethod {
        private String methodName;
        private String returnType;
        private String dispatch;
        private List<MethodParameter> parameters = new ArrayList<>();

    }

    protected static class MethodParameter {
        private String name;
        private String type;
        private AssignmentType assignmentType;
        private String assignmentExpression;
    }
}
