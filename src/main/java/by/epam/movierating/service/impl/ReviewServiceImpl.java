package by.epam.movierating.service.impl;

import by.epam.movierating.bean.Review;
import by.epam.movierating.dao.ReviewDAO;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.factory.DAOFactory;
import by.epam.movierating.service.ReviewService;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;

/**
 * @author serge
 *         30.06.2017.
 */
public class ReviewServiceImpl implements ReviewService {

    @Override
    public List<Review> getReviewsByMovieId(int idMovie, String language) throws ServiceException {
        List<Review> reviewList;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            ReviewDAO reviewDAO = daoFactory.getReviewDAO();
            reviewList = reviewDAO.getReviewsByMovieId(idMovie,language);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting list of reviews by movieId",e);
        }
        return reviewList;
    }
}
