package soya.framework.restruts.action;

import java.io.Serializable;
import java.util.Objects;

public final class ActionName implements Serializable {

    private final String domain;
    private final String name;

    private ActionName(String domain, String name) {
        this.domain = domain;
        this.name = name;

        if (domain == null || domain.trim().length() == 0) {
            throw new IllegalArgumentException("Domain cannot be null or empty");
        }

        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    public String getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return domain + "://" + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionName)) return false;
        ActionName that = (ActionName) o;
        return Objects.equals(domain, that.domain) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, name);
    }

}
