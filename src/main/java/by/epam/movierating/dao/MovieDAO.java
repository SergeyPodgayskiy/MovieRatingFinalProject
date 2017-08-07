package by.epam.movierating.dao;

import by.epam.movierating.bean.Movie;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.util.ColumnExistable;
import by.epam.movierating.dao.util.DAODefaultFunctional;
import by.epam.movierating.dao.util.JDBCAutocloseable;

import java.util.List;

/**
 * @author serge
 *         02.06.2017.
 */
public interface MovieDAO extends DAODefaultFunctional, JDBCAutocloseable, ColumnExistable {

    int addMovie(Movie movie) throws DAOException;

    boolean addLocalizedMovieInfo(Movie movie, String contentLanguage) throws DAOException;

    boolean addGenreForMovie(int idMovie, int genreId) throws DAOException;

    boolean addCountryForMovie(int movieId, String countryCode) throws DAOException;

    boolean addParticipantForMovie(int idMovie, int idParticipant, int movieroleId) throws DAOException;

    List<Movie> getAllMovies() throws DAOException;

    List<Movie> getTopMovies(String language) throws DAOException;

    List<Movie> getMostDiscussedMovies(String language) throws DAOException;

    Movie getMovieById(int idMovie, String language) throws DAOException;

    List<Movie> getMoviesByGenreId(int idGenre) throws DAOException;

    List<Movie> getMoviesByGenreName(String genreName) throws DAOException;

    List<Movie> getMoviesByCountryCode(String countryCode) throws DAOException;

    List<Movie> getMoviesByCountryName(String countryName) throws DAOException;

    List<Movie> getNewestMovies() throws DAOException;

    boolean deleteGenreForMovie(int movieId, int genreId) throws DAOException;

    boolean deleteCountryForMovie(int movieId, String countryCode) throws DAOException;

    boolean deleteParticipantForMovie(int movieId,int participantId, int movieroleId)
            throws DAOException;

    boolean deleteMovie(int idMovie) throws DAOException;

    boolean uploadMoviePoster(int idMovie, String imgURL) throws DAOException;

    boolean updateMovie(Movie movie) throws DAOException;

    boolean checkLocalizedMovieInfo(int movieId, String languageCode)
            throws DAOException;

    Movie getLocalizedMovieInfo(int movieId, String languageCode) throws DAOException;

    boolean updateLocalizedMovieInfo(Movie movie, String contentLanguage) throws DAOException;

    List<Movie> getMoviesByParticipantId(int idParticipant, String currentLanguage) throws DAOException;
}
