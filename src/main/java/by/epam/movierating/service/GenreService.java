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

    int addGenre(String name, String description, String contentLanguage) throws ServiceException;

    boolean addLocalizedGenreInfo(int genreId, String name, String description, String contentLanguage)
            throws ServiceException;

    boolean updateLocalizedGenreInfo(int genreId, String name, String description, String contentLanguage)
            throws ServiceException;

    boolean deleteGenreById(int genreId) throws ServiceException;

    boolean checkLocalizedGenreInfoByCode(int genreId, String languageCode) throws ServiceException;

    Genre getLocalizedGenreInfoByCode(int genreId, String languageCode) throws ServiceException;
}
