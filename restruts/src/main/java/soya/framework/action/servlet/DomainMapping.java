package soya.framework.action.servlet;

import java.io.Serializable;
import java.util.*;

public class DomainMapping implements Comparable<DomainMapping>, Serializable {

    private final String name;
    private final String path;
    private final String title;
    private final String description;

    private transient Set<ActionMapping> actionMappings = new HashSet<>();

    public DomainMapping(String name, String path, String title, String description) {
        this.name = name;
        this.path = path;
        this.title = title;
        this.description = description;
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

    public void add(ActionMapping actionMapping) {
        actionMappings.add(actionMapping);
    }

    public List<ActionMapping> getActionMappings() {
        List<ActionMapping> list = new ArrayList<>(actionMappings);
        Collections.sort(list);
        return list;
    }

    @Override
    public int compareTo(DomainMapping o) {
        return path.compareTo(o.path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainMapping)) return false;
        DomainMapping that = (DomainMapping) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

