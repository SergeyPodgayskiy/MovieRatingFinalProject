package by.epam.movierating.service;

import by.epam.movierating.bean.Review;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;

/**
 * @author serge
 *         30.06.2017.
 */
public interface ReviewService {
    List<Review> getReviewsByMovieId(int idMovie, String language) throws ServiceException;
}
