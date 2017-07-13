package by.epam.movierating.bean;

import java.io.Serializable;

/**
 * @author serge
 *         01.06.2017.
 */
public class Country implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String iconURL;

    public Country() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
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

        Country country = (Country) obj;

        if (code != null ? !code.equals(country.code) : country.code != null) {
            return false;
        }
        return name != null ? name.equals(country.name) : country.name == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", iconURL='" + iconURL + '\'' +
                '}';
    }
}
