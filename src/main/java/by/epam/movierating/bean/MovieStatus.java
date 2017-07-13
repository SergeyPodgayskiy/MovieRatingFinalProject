package by.epam.movierating.bean;

import java.io.Serializable;

/**
 * @author serge
 *         02.06.2017.
 */
public class MovieStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idUser; //todo may be delete this two id fields
    private int idMovie;
    private boolean isFavorite;
    private boolean isWatched;
    private boolean willWatch;

    public MovieStatus() {
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public boolean isWillWatch() {
        return willWatch;
    }

    public void setWillWatch(boolean willWatch) {
        this.willWatch = willWatch;
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

        MovieStatus movieStatus = (MovieStatus) obj;

        if (idMovie != movieStatus.idMovie) {
            return false;
        }
        if (idUser != movieStatus.idUser) {
            return false;
        }
        if (isFavorite != movieStatus.isFavorite) {
            return false;
        }
        if (isWatched != movieStatus.isWatched) {
            return false;
        }
        if (willWatch != movieStatus.willWatch) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = idUser;
        result = 31 * result + idMovie;
        result = 31 * result + (isFavorite ? 1 : 0);
        result = 31 * result + (isWatched ? 1 : 0);
        result = 31 * result + (willWatch ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "idUser=" + idUser +
                ", idMovie=" + idMovie +
                ", isFavorite=" + isFavorite +
                ", isWatched=" + isWatched +
                ", willWatch=" + willWatch +
                '}';
    }
}
