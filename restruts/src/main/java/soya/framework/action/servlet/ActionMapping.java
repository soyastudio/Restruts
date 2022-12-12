package soya.framework.action.servlet;

import soya.framework.action.ActionName;
import soya.framework.action.ParameterType;
import soya.framework.commons.util.StreamUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ActionMapping implements Comparable<ActionMapping>, Serializable {
    private final ActionName actionName;
    private final String httpMethod;
    private final String path;
    private final String produce;

    private List<String> descriptions = new ArrayList<>();
    private List<ParameterMapping> parameters = new ArrayList<>();

    private PathMapping pathMapping;

    ActionMapping(ActionName actionName, String httpMethod, String path, String produce) {
        this.actionName = actionName;
        this.httpMethod = httpMethod;
        this.path = path;
        this.produce = produce;

        this.pathMapping = new PathMapping(path);
    }

    public ActionName getActionName() {
        return actionName;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public String getProduce() {
        return produce;
    }

    public void addDescriptions(String... line) {
        if(line != null) {
            descriptions.addAll(Arrays.asList(line));
        }
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder();
        descriptions.forEach(e -> {
            builder.append(e).append("\n");
        });
        return builder.toString();
    }

    public List<ParameterMapping> getParameters() {
        return parameters;
    }

    public PathMapping getPathMapping() {
        return pathMapping;
    }

    public boolean match(HttpServletRequest request) {
        return httpMethod.equalsIgnoreCase(request.getMethod()) && pathMapping.match(request.getPathInfo());
    }

    public Object getParameterValue(HttpServletRequest request, ParameterMapping parameterMapping) {
        String paramName = parameterMapping.getName();
        ParameterType paramType = parameterMapping.getParameterType();
        String contentType = parameterMapping.getContentType();

        switch (paramType) {
            case PATH_PARAM:
                return getFromPath(request, parameterMapping);

            case QUERY_PARAM:
                return getFromQuery(request, parameterMapping);

            case HEADER_PARAM:
                return request.getHeader(paramName);

            case COOKIE_PARAM:
                return getFromCookie(request, parameterMapping);

            case FORM_PARAM:
                return getFromForm(request, parameterMapping);

            case MATRIX_PARAM:
                return getFromMatrix(request, parameterMapping);

            case BEAN_PARAM:
                return getFromBean(request, parameterMapping);

            case PAYLOAD:
                return getFromPayload(request, contentType);

            default:
                throw new RuntimeException("Not supported.");
        }
    }

    private String getFromPath(HttpServletRequest request, ParameterMapping parameterMapping) {
        String token = "{" + parameterMapping.getName() + "}";
        String[] items = getPathMapping().getItems();
        String[] values = new PathMapping(request.getPathInfo()).getItems();
        for(int i = 0; i < items.length; i ++) {
            if(items[i].contains(token)) {
                return parse(items[i], token, values[i]);
            }
        }

        return null;
    }

    private String parse(String pattern, String token, String value) {
        int index = pattern.indexOf(token);

        String prefix = pattern.substring(0, index);
        String suffix = pattern.substring(index + token.length());

        if(prefix.contains("{") && prefix.contains("}") || suffix.contains("{") && suffix.contains("}")) {
            throw new IllegalArgumentException("Multiple path parameters in one path item is not supported.");
        }

        if(value.startsWith(prefix) && value.endsWith(suffix)) {
            return value.substring(index, value.length() - suffix.length());
        }

        return null;
    }

    private String getFromQuery(HttpServletRequest request, ParameterMapping parameterMapping) {
        return request.getParameter(parameterMapping.getName());
    }

    private String getFromHeader(HttpServletRequest request, ParameterMapping parameterMapping) {
        return request.getHeader(parameterMapping.getName());
    }

    private String getFromCookie(HttpServletRequest request, ParameterMapping parameterMapping) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(parameterMapping.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    private String getFromForm(HttpServletRequest request, ParameterMapping parameterMapping) {
        throw new UnsupportedOperationException("Form param is not supported yet.");
    }

    private String getFromMatrix(HttpServletRequest request, ParameterMapping parameterMapping) {
        throw new UnsupportedOperationException("Matrix param is not supported yet.");
    }

    private String getFromBean(HttpServletRequest request, ParameterMapping parameterMapping) {
        throw new UnsupportedOperationException("Bean param is not supported yet.");
    }

    private Object getFromPayload(HttpServletRequest request, String contentType) {
        try {
            byte[] bin = StreamUtils.copyToByteArray(request.getInputStream());
            return new String(bin);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionMapping)) return false;
        ActionMapping that = (ActionMapping) o;
        return actionName.equals(that.actionName) && httpMethod.equals(that.httpMethod) && path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionName, httpMethod, path);
    }

    @Override
    public int compareTo(ActionMapping o) {
        int result = this.path.compareTo(o.path);

        return result;
    }
}

