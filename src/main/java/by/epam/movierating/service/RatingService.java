package by.epam.movierating.service;

import by.epam.movierating.bean.Rating;
import by.epam.movierating.service.exception.ServiceException;

import java.math.BigDecimal;

/**
 * @author serge
 *         30.06.2017.
 */
public interface RatingService {
    Rating getUserMovieRatingByUserId(int movieId, int userId) throws ServiceException;

    boolean rateMovie(int movieId, int userId, BigDecimal mark) throws ServiceException;

    boolean updateMovieRating(int movieId, int userId, BigDecimal mark) throws ServiceException;

    boolean deleteMovieRating(int movieId, int userId) throws ServiceException;
}
