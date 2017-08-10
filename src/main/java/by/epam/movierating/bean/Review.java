package by.epam.movierating.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author serge
 *         01.06.2017.
 */
public class Review implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idUser;
    private int idMovie;
    private Date publicationDate;
    private String type;
    private String title;
    private String text;

    public Review() {
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

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

        Review review = (Review) obj;

        if (idMovie != review.idMovie) {
            return false;
        }
        if (idUser != review.idUser) {
            return false;
        }
        if (publicationDate != null ? !publicationDate.equals(review.publicationDate) :
                review.publicationDate != null) {
            return false;
        }
        if (type != null ? !type.equals(review.type) : review.type != null) {
            return false;
        }
        if (title != null ? !title.equals(review.title) : review.title != null) {
            return false;
        }
        return text != null ? text.equals(review.text) : review.text == null;
    }

    @Override
    public int hashCode() {
        int result = idUser;
        result = 31 * result + idMovie;
        result = 31 * result + (publicationDate != null ? publicationDate.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "idUser=" + idUser +
                ", idMovie=" + idMovie +
                ", publicationDate=" + publicationDate +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
