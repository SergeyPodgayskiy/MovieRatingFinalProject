package by.epam.movierating.service;

import by.epam.movierating.bean.dto.ReviewDTO;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;

/**
 * @author serge
 *         30.06.2017.
 */
public interface ReviewService {
    List<ReviewDTO> getReviewsDTOByMovieId(int idMovie) throws ServiceException;

    boolean reviewMovie(int idMovie, int idUser, String title,
                        String text, String type) throws ServiceException;

    boolean checkReviewOpportunity(int idMovie, Integer idUser) throws ServiceException;

    boolean deleteReview(int idMovie, int idUser) throws ServiceException;

    boolean updateReview(int idMovie, int idUser, String title, String text, String type)
            throws ServiceException;
}
