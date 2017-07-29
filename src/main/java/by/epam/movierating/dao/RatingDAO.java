package by.epam.movierating.dao;

import by.epam.movierating.bean.Rating;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.util.ColumnExistable;
import by.epam.movierating.dao.util.DAODefaultFunctional;
import by.epam.movierating.dao.util.JDBCAutocloseable;

/**
 * @author serge
 *         02.06.2017.
 */
public interface RatingDAO extends DAODefaultFunctional, JDBCAutocloseable, ColumnExistable {
    boolean rateMovie(Rating rating) throws DAOException;

    Rating getUserMovieRatingByUserId(int idMovie, int idUser) throws DAOException;

    boolean deleteRating(int movieId, int userId) throws DAOException;

    boolean checkRateOpportunity(int idMovie, int idUser) throws DAOException;

    boolean updateMovieRating(Rating rating) throws DAOException;
}
