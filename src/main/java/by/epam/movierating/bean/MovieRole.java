package by.epam.movierating.bean;

import java.io.Serializable;

/**
 * @author serge
 *         02.06.2017.
 */
public class MovieRole implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;

    public MovieRole() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        MovieRole movieRole = (MovieRole) obj;

        if (id != movieRole.id) {
            return false;
        }
        return name != null ?
                name.equals(movieRole.name) :
                movieRole.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
