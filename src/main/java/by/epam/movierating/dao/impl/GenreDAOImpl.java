package by.epam.movierating.dao.impl;

import by.epam.movierating.bean.Genre;
import by.epam.movierating.dao.GenreDAO;
import by.epam.movierating.dao.connectionpool.ConnectionPool;
import by.epam.movierating.dao.exception.ConnectionPoolException;
import by.epam.movierating.dao.exception.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author serge
 *         02.06.2017.
 */
public class GenreDAOImpl implements GenreDAO {
    private static final String SQL_ADD_GENRE =
            "INSERT INTO `genre` (name) VALUES (?)";
    private static final String SQL_GET_GENRE_BY_ID =
            "SELECT * FROM `genre` WHERE id=?";
    private static final String SQL_GET_ALL_GENRES =
            "SELECT genre.id, genre_localization.name" +
                    " FROM `genre`" +
                    " INNER JOIN genre_localization" +
                    " ON genre.id = genre_localization.id_genre " +
                    " AND genre_localization.language_code = ?";
    private static final String SQL_UPDATE_GENRE =
            "UPDATE genre SET name=? WHERE id=?";
    private static final String SQL_DELETE_GENRE =
            "UPDATE genre SET deleted_at=? WHERE id=?";
    private static final String SQL_GET_GENRE_BY_MOVIE_ID =
            "SELECT genre.id, genre_loc.name" +
                    " FROM genre " +
                    "INNER JOIN genre_localization AS genre_loc ON genre.id = genre_loc.id_genre " +
                    "INNER JOIN movie_genre ON genre.id = movie_genre.id_genre " +
                    "AND genre_loc.language_code = ? " +
                    "WHERE movie_genre.id_movie = ?";

    @Override
    public int addGenre(String name) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int genreId = -1;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_GENRE,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
            if (isAdded) {
                genreId = returnGeneratedId(preparedStatement);
            } else {
                throw new DAOException("Creating genre failed, no rows added.");
            }
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_GENRE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return genreId;
    }

    @Override
    public Genre getGenreById(int idGenre) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Genre genre;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_GENRE_BY_ID);
            preparedStatement.setInt(1, idGenre);
            resultSet = preparedStatement.executeQuery();
            genre = setDataForOneGenre(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_GENRE_BY_ID query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return genre;
    }

    @Override
    public List<Genre> getGenresByMovieId(int idMovie, String language) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Genre> genreList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_GENRE_BY_MOVIE_ID);
            preparedStatement.setString(1, language);
            preparedStatement.setInt(2, idMovie);
            resultSet = preparedStatement.executeQuery();
            genreList = setDataForGenres(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_GENRE_BY_MOVIE_ID query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return genreList;
    }

    @Override
    public List<Genre> getAllGenres(String language) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Genre> genreList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_GENRES);
            preparedStatement.setString(1, language);
            resultSet = preparedStatement.executeQuery();
            genreList = setDataForGenres(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_ALL_GENRES query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return genreList;
    }

    @Override
    public boolean updateGenre(Genre genre) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_GENRE);
            preparedStatement.setString(1, genre.getName());
            preparedStatement.setInt(2, genre.getId());
            isUpdated = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_GENRE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteGenre(int idGenre) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isDeleted;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_GENRE);
            preparedStatement.setInt(1, idGenre);
            isDeleted = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_DELETE_GENRE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isDeleted;
    }

    private Genre setDataForOneGenre(ResultSet resultSet) throws SQLException {
        Genre genre = null;
        if (resultSet.next()) {
            genre = createGenre(resultSet);
        }
        return genre;
    }

    private List<Genre> setDataForGenres(ResultSet resultSet) throws SQLException {
        List<Genre> genreList = new ArrayList<>();
        while (resultSet.next()) {
            Genre genre = createGenre(resultSet);
            genreList.add(genre);
        }
        return genreList;
    }

    private Genre createGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();

        genre.setId(isColumnExist(Column.ID, rs) ?
                rs.getInt(Column.ID) : -1);
        genre.setName(isColumnExist(Column.NAME, rs) ?
                rs.getString(Column.NAME) : null);

        return genre;
    }

    private class Column {
        private static final String ID = "id";
        private static final String NAME = "name";
    }
}
