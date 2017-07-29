package by.epam.movierating.service;

import by.epam.movierating.bean.Genre;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;

/**
 * @author serge
 *         30.06.2017.
 */
public interface GenreService {
    List<Genre> getGenresByMovieId(int idMovie, String language) throws ServiceException;

    List<Genre> getAllGenres(String language) throws ServiceException;
}
