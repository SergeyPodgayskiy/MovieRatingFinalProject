package by.epam.movierating.service.impl;

import by.epam.movierating.bean.Review;
import by.epam.movierating.bean.dto.ReviewDTO;
import by.epam.movierating.dao.ReviewDAO;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.factory.DAOFactory;
import by.epam.movierating.service.ReviewService;
import by.epam.movierating.service.exception.ServiceException;

import java.util.Date;
import java.util.List;

/**
 * @author serge
 *         30.06.2017.
 */
public class ReviewServiceImpl implements ReviewService {

    @Override
    public List<ReviewDTO> getReviewsDTOByMovieId(int idMovie) throws ServiceException {
        List<ReviewDTO> reviewList;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            ReviewDAO reviewDAO = daoFactory.getReviewDAO();
            reviewList = reviewDAO.getReviewsDTOByMovieId(idMovie);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting list of reviews by movieId",e);
        }
        return reviewList;
    }

    @Override
    public boolean reviewMovie(int idMovie, int idUser, String title, String text, String type)
            throws ServiceException {

       boolean isAdded;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            ReviewDAO reviewDAO = daoFactory.getReviewDAO();
            Review review = new Review();
            review.setIdMovie(idMovie);
            review.setIdUser(idUser);
            review.setTitle(title);
            review.setText(text);
            review.setType(type);
            review.setPublicationDate(new Date());
            isAdded = reviewDAO.reviewMovie(review);
        } catch (DAOException e) {
            throw new ServiceException("Error during adding review for movie",e);
        }
        return isAdded;
    }

    @Override
    public boolean checkReviewOpportunity(int idMovie, Integer idUser)
            throws ServiceException {

        boolean isReviewed;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            ReviewDAO reviewDAO = daoFactory.getReviewDAO();
            isReviewed = reviewDAO.checkReviewOpportunity(idMovie, idUser);
        } catch (DAOException e) {
            throw new ServiceException("Error during checking review opportunity",e);
        }
        return isReviewed;
    }

    @Override
    public boolean deleteReview(int idMovie, int idUser) throws ServiceException {

        boolean isDeleted;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            ReviewDAO reviewDAO = daoFactory.getReviewDAO();
            isDeleted = reviewDAO.deleteReview(idMovie, idUser);
        } catch (DAOException e) {
            throw new ServiceException("Error during deleting review",e);
        }
        return isDeleted;
    }

    @Override
    public boolean updateReview(int idMovie, int idUser, String title, String text, String type)
            throws ServiceException {

        boolean isUpdated;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            ReviewDAO reviewDAO = daoFactory.getReviewDAO();
            Review review = new Review();
            review.setIdMovie(idMovie);
            review.setIdUser(idUser);
            review.setTitle(title);
            review.setText(text);
            review.setType(type);
            isUpdated = reviewDAO.updateReview(review);
        } catch (DAOException e) {
            throw new ServiceException("Error during updating review for movie",e);
        }
        return isUpdated;
    }
}
