package by.epam.movierating.service;

import by.epam.movierating.bean.Movie;
import by.epam.movierating.service.exception.ServiceException;

import java.sql.Time;
import java.util.List;

/**
 * @author serge
 *         23.06.2017.
 */
public interface MovieService {

    List<Movie> getTopMovies(String language) throws ServiceException;

    List<Movie> getMostDiscussedMovies(String language) throws ServiceException;

    Movie getMovieById(int idMovie, String language) throws ServiceException;

    int addMovie(int releaseYear, String ageLimit, Time duration) throws ServiceException;

    boolean addCountryForMovie(int movieId, String countryCode) throws ServiceException;

    boolean deleteCountryForMovie(int movieId, String countryCode) throws ServiceException;

    boolean addLocalizedMovieInfo(int movieId, String contentLanguage,
                                  String movieName,
                                  String description, String slogan) throws ServiceException;

    boolean addParticipantForMovie(int movieId, int participantId, int movieroleId)
            throws ServiceException;

    boolean deleteParticipantForMovie(int movieId, int participantId, int movieroleId)
            throws ServiceException;

    boolean uploadMoviePoster(int movieId, String imgPath) throws ServiceException;

    boolean addGenreForMovie(int movieId, int genreId) throws ServiceException;

    boolean deleteGenreForMovie(int movieId, int genreId) throws ServiceException;

    boolean updateMovie(int movieId, int releaseYear, String ageLimit, Time duration)
            throws ServiceException;

    boolean checkLocalizedMovieInfo(int movieId, String languageCode) throws ServiceException;

    Movie getLocalizedMovieInfo(int movieId, String languageCode) throws ServiceException;

    boolean updateLocalizedMovieInfo(int movieId,
                                     String contentLanguage,
                                     String movieName,
                                     String description,
                                     String slogan) throws ServiceException;

    boolean deleteMovieById(int movieId) throws ServiceException;
}
