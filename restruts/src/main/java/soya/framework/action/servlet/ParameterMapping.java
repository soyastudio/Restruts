package soya.framework.action.servlet;

import soya.framework.action.MediaType;
import soya.framework.action.ParameterType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ParameterMapping implements Serializable {
    private final String name;
    private final ParameterType parameterType;
    private List<String> descriptions = new ArrayList<>();
    private String contentType = MediaType.TEXT_PLAIN;

    public ParameterMapping(String name, ParameterType parameterType) {
        this.name = name;
        this.parameterType = parameterType;
    }

    public String getName() {
        return name;
    }

    public ParameterType getParameterType() {
        return parameterType;
    }

    public void addDescriptions(String... lines) {
        if (lines != null) {
            descriptions.addAll(Arrays.asList(lines));
        }
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder();
        descriptions.forEach(e -> {
            builder.append(e).append("\n");
        });
        return builder.toString();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterMapping)) return false;
        ParameterMapping that = (ParameterMapping) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
