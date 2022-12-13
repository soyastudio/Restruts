package soya.framework.action.dispatch;

import java.io.Serializable;
import java.util.Objects;

public final class DynaDomain implements Comparable<DynaDomain>, Serializable {
    private final String name;
    private final String path;
    private final String title;
    private final String description;

    public DynaDomain(String name) {
        this.name = name;
        this.path = "/" + name;
        this.title = name;
        this.description = name;
    }

    public DynaDomain(String name, String path, String title, String description) {
        this.name = name;
        this.path = path != null ? path : "/" + name;
        this.title = title != null ? title : name;
        this.description = description != null ? description : "";
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DynaDomain)) return false;
        DynaDomain that = (DynaDomain) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(DynaDomain o) {
        return this.path.compareTo(o.path);
    }
}
