package by.epam.movierating.dao;

import by.epam.movierating.bean.Genre;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.util.ColumnExistable;
import by.epam.movierating.dao.util.DAODefaultFunctional;
import by.epam.movierating.dao.util.JDBCAutocloseable;

import java.util.List;

/**
 * @author serge
 *         02.06.2017.
 */
public interface GenreDAO extends DAODefaultFunctional, JDBCAutocloseable, ColumnExistable {
    int addGenre(String name) throws DAOException;

    Genre getGenreById(int idGenre) throws DAOException;

    List<Genre> getGenresByMovieId(int idMovie, String language) throws DAOException;

    List<Genre> getAllGenres(String language) throws DAOException;

    boolean updateGenre(Genre genre) throws DAOException;

    boolean deleteGenre(int idGenre) throws DAOException;
}
