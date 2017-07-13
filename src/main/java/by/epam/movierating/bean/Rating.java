package by.epam.movierating.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author serge
 *         01.06.2017.
 */
public class Rating implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idMovie;
    private int idUser;
    private BigDecimal mark;

    public Rating() {
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public BigDecimal getMark() {
        return mark;
    }

    public void setMark(BigDecimal mark) {
        this.mark = mark;
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

        Rating rating = (Rating) obj;

        if (idMovie != rating.idMovie) {
            return false;
        }
        if (idUser != rating.idUser) {
            return false;
        }
        if (mark != rating.mark) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = idMovie;
        result = 31 * result + idUser;
        result = 31 * result + (mark != null ? mark.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "idMovie=" + idMovie +
                ", idUser=" + idUser +
                ", mark=" + mark +
                '}';
    }
}
