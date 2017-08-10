package by.epam.movierating.bean.dto;

import by.epam.movierating.bean.Review;

import java.io.Serializable;

/**
 * @author serge
 *         08.08.2017.
 */
public class ReviewDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userLogin;
    private Review review;

    public ReviewDTO() {
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
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
        ReviewDTO reviewDTO = (ReviewDTO) obj;

        if (userLogin != null ? !userLogin.equals(reviewDTO.userLogin) : reviewDTO.userLogin != null) {
            return false;
        }

        return review != null ? review.equals(reviewDTO.review) : reviewDTO.review == null;
    }

    @Override
    public int hashCode() {
        int result = userLogin != null ? userLogin.hashCode() : 0;
        result = 31 * result + (review != null ? review.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                ", userLogin='" + userLogin + '\'' +
                ", review=" + review +
                '}';
    }
}
