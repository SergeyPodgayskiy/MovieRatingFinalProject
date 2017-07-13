package by.epam.movierating.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author serge
 *         02.06.2017.
 */
public class MovieParticipant implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String surname;
    private Date birthDate;
    private Country country;
    private String photoURL;
    private Date deletedAt;
    private int amountOfMovies;
    private ParticipantMovieRole participantMovieRole;

    public MovieParticipant() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public ParticipantMovieRole getParticipantMovieRole() {
        return participantMovieRole;
    }

    public void setParticipantMovieRole(ParticipantMovieRole participantMovieRole) {
        this.participantMovieRole = participantMovieRole;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public int getAmountOfMovies() {
        return amountOfMovies;
    }

    public void setAmountOfMovies(int amountOfMovies) {
        this.amountOfMovies = amountOfMovies;
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

        MovieParticipant participant = (MovieParticipant) obj;

        if (id != participant.id) {
            return false;
        }
        if (amountOfMovies != participant.amountOfMovies) {
            return false;
        }
        if (name != null ? !name.equals(participant.name) : participant.name != null) {
            return false;
        }
        if (surname != null ? !surname.equals(participant.surname) : participant.surname != null) {
            return false;
        }
        if (birthDate != null ? !birthDate.equals(participant.birthDate) : participant.birthDate != null) {
            return false;
        }
        if (country != null ? !country.equals(participant.country) : participant.country != null) {
            return false;
        }
        if (photoURL != null ? !photoURL.equals(participant.photoURL) : participant.photoURL != null) {
            return false;
        }
        if (deletedAt != null ? !deletedAt.equals(participant.deletedAt) : participant.deletedAt != null) {
            return false;
        }
        return participantMovieRole != null ?
                participantMovieRole.equals(participant.participantMovieRole) : participant.participantMovieRole == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (photoURL != null ? photoURL.hashCode() : 0);
        result = 31 * result + (deletedAt != null ? deletedAt.hashCode() : 0);
        result = 31 * result + amountOfMovies;
        result = 31 * result + (participantMovieRole != null ? participantMovieRole.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MovieParticipant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", country=" + country +
                ", photoURL='" + photoURL + '\'' +
                ", deletedAt=" + deletedAt +
                ", amountOfMovies=" + amountOfMovies +
                ", participantMovieRole=" + participantMovieRole +
                '}';
    }
}
