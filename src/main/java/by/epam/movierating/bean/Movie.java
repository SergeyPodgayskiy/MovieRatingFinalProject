package by.epam.movierating.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

/**
 * @author serge
 *         01.06.2017.
 */
public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private String description;
    private BigDecimal rating;
    private int amountOfReviews;
    private int amountOfMarks;
    private Date releaseYear;
    private String slogan;
    private String posterURL;
    private Time duration;
    private String ageLimit;
    private MovieStatus movieStatus;
    private Date deletedAt;

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Date releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public String getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(String ageLimit) {
        this.ageLimit = ageLimit;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public int getAmountOfReviews() {
        return amountOfReviews;
    }

    public int getAmountOfMarks() {
        return amountOfMarks;
    }

    public void setAmountOfMarks(int amountOfMarks) {
        this.amountOfMarks = amountOfMarks;
    }

    public void setAmountOfReviews(int amountOfReviews) {
        this.amountOfReviews = amountOfReviews;
    }

    public MovieStatus getMovieStatus() {
        return movieStatus;
    }

    public void setMovieStatus(MovieStatus movieStatus) {
        this.movieStatus = movieStatus;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
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

        Movie movie = (Movie) obj;

        if (id != movie.id) {
            return false;
        }
        if (title != null ? !title.equals(movie.title) : movie.title != null) {
            return false;
        }
        if (description != null ? !description.equals(movie.description) : movie.description != null) {
            return false;
        }
        if (releaseYear != null ? !releaseYear.equals(movie.releaseYear) : movie.releaseYear != null) {
            return false;
        }
        return duration != null ? !duration.equals(movie.duration) : movie.duration != null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (releaseYear != null ? releaseYear.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                ", amountOfReviews=" + amountOfReviews +
                ", amountOfMarks=" + amountOfMarks +
                ", releaseYear=" + releaseYear +
                ", slogan='" + slogan + '\'' +
                ", posterURL='" + posterURL + '\'' +
                ", duration=" + duration +
                ", ageLimit='" + ageLimit + '\'' +
                ", movieStatus=" + movieStatus +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
