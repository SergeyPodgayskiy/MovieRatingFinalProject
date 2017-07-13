package by.epam.movierating.service.impl;

import by.epam.movierating.bean.Rating;
import by.epam.movierating.dao.RatingDAO;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.factory.DAOFactory;
import by.epam.movierating.service.RatingService;
import by.epam.movierating.service.exception.ServiceException;

import java.math.BigDecimal;

/**
 * @author serge
 *         30.06.2017.
 */
public class RatingServiceImpl implements RatingService {
    @Override
    public Rating getUserMovieRatingByUserId(int movieId,int userId) throws ServiceException {
        //todo validate
        Rating rating;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            RatingDAO ratingDAO = daoFactory.getRatingDAO();
            rating = ratingDAO.getUserMovieRatingByUserId(movieId, userId);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting user movie rating by userId", e);
        }
        return rating;
    }

    @Override
    public boolean rateMovie(int movieId, int userId, BigDecimal mark) throws ServiceException {
        Rating rating;
        boolean isRated;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            RatingDAO ratingDAO = daoFactory.getRatingDAO();

            rating = new Rating();
            rating.setIdMovie(movieId);
            rating.setIdUser(userId);
            rating.setMark(mark);
            isRated = ratingDAO.rateMovie(rating);
        } catch (DAOException e) {
            throw new ServiceException("Error during rate movie procedure", e);
        }
        return isRated;
    }

    @Override
    public boolean updateMovieRating(int movieId, int userId, BigDecimal mark) throws ServiceException {
        //todo validation
        Rating rating;
        boolean isRated;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            RatingDAO ratingDAO = daoFactory.getRatingDAO();

            rating = new Rating();
            rating.setIdMovie(movieId);
            rating.setIdUser(userId);
            rating.setMark(mark);
            isRated = ratingDAO.updateMovieRating(rating);
        } catch (DAOException e) {
            throw new ServiceException("Error during update movie rating procedure", e);
        }
        return isRated;
    }

    @Override
    public boolean deleteMovieRating(int movieId, int userId) throws ServiceException {
        //todo validation
        boolean isDeleted;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            RatingDAO ratingDAO = daoFactory.getRatingDAO();
            isDeleted = ratingDAO.deleteRating(movieId,userId);
        } catch (DAOException e) {
            throw new ServiceException("Error during delete movie rating procedure", e);
        }
        return isDeleted;
    }
}
