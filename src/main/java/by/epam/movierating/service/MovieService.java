package by.epam.movierating.service;

import by.epam.movierating.bean.Movie;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;

/**
 * @author serge
 *         23.06.2017.
 */
public interface MovieService {

    List<Movie> getTopMovies(String language) throws ServiceException;

    List<Movie> getMostDiscussedMovies(String language) throws ServiceException;

    Movie getMovieById(int idMovie, String language) throws ServiceException;
}
