package by.epam.movierating.dao;

import by.epam.movierating.bean.Movie;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.util.DAODefaultFunctional;
import by.epam.movierating.dao.util.JDBCAutocloseable;

import java.util.List;

/**
 * @author serge
 *         02.06.2017.
 */
public interface MovieDAO extends DAODefaultFunctional, JDBCAutocloseable {
    int addMovie(Movie movie) throws DAOException;

    boolean addGenreForMovie(int idMovie, String genreName) throws DAOException;

    boolean addCountryForMovie(int idMovie, String countryName) throws DAOException;

    boolean addParticipantForMovie(int idMovie, String firstName, String lastName)
            throws DAOException;

    List<Movie> getAllMovies() throws DAOException;

    List<Movie> getTopMovies(String language) throws DAOException;

    List<Movie> getMostDiscussedMovies(String language) throws DAOException;

    Movie getMovieById(int idMovie, String language) throws DAOException;

    List<Movie> getMoviesByGenreId(int idGenre) throws DAOException;

    List<Movie> getMoviesByGenreName(String genreName) throws DAOException;

    List<Movie> getMoviesByCountryCode(String countryCode) throws DAOException;

    List<Movie> getMoviesByCountryName(String countryName) throws DAOException;

    List<Movie> getNewestMovies() throws DAOException;

    boolean updateMovie(Movie movie) throws DAOException;

    boolean deleteGenreForMovie(int movieId, String genreName) throws DAOException;

    boolean deleteCountryForMovie(int movieId, String countryName) throws DAOException;

    boolean deleteParticipantForMovie(int movieId, String firstName, String lastName)
            throws DAOException;

    boolean deleteMovie(int idMovie) throws DAOException;

    boolean uploadMoviePoster(int idMovie, String imgURL) throws DAOException;
}
