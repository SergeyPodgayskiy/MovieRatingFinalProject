package by.epam.movierating.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author serge
 *         08.05.2017.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String login;
    private String password;
    private String email;
    private boolean isAdmin;
    private boolean isBanned;
    private Date registerDate;
    private String fullName;
    private Date birthDate;
    private Country country;
    private String photoURL;
    private int amountOfMarks;
    private int amountOfReviews;
    private Date deletedAt;

    public User() {
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public int getAmountOfMarks() {
        return amountOfMarks;
    }

    public void setAmountOfMarks(int amountOfMarks) {
        this.amountOfMarks = amountOfMarks;
    }

    public int getAmountOfReviews() {
        return amountOfReviews;
    }

    public void setAmountOfReviews(int amountOfReviews) {
        this.amountOfReviews = amountOfReviews;
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

        User user = (User) obj;

        if (id != user.id) {
            return false;
        }
        if (login != null ? !login.equals(user.login) : user.login != null) {
            return false;
        }
        if (password != null ? !password.equals(user.password) : user.password != null) {
            return false;
        }
        if (fullName != null ? !fullName.equals(user.fullName) : user.fullName != null) {
            return false;
        }
        if (email != null ? !email.equals(user.email) : user.email != null) {
            return false;
        }
        return registerDate != null ? registerDate.equals(user.registerDate) : user.registerDate == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (registerDate != null ? registerDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", isBanned=" + isBanned +
                ", registerDate=" + registerDate +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                ", country=" + country +
                ", photoURL='" + photoURL + '\'' +
                ", amountOfMarks=" + amountOfMarks +
                ", amountOfReviews=" + amountOfReviews +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
