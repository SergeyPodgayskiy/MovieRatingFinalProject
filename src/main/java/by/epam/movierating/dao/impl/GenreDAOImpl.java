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
public  class GenreDAOImpl implements GenreDAO {
    private static final String SQL_ADD_GENRE =
            "INSERT INTO `genre` (id) VALUES(DEFAULT)";
    private static final String SQL_GET_GENRE_BY_ID =
            "SELECT * FROM `genre` WHERE id=?";
    private static final String SQL_GET_ALL_GENRES =
            "SELECT genre.id, genre_localization.name" +
                    " FROM `genre`" +
                    " INNER JOIN genre_localization" +
                    " ON genre.id = genre_localization.id_genre " +
                    " AND genre_localization.language_code = ?";
    private static final String SQL_DELETE_GENRE =
            "DELETE FROM genre WHERE id=?";
    private static final String SQL_GET_GENRE_BY_MOVIE_ID =
            "SELECT genre.id, genre_loc.name" +
                    " FROM genre " +
                    "INNER JOIN genre_localization AS genre_loc ON genre.id = genre_loc.id_genre " +
                    "INNER JOIN movie_genre ON genre.id = movie_genre.id_genre " +
                    "AND genre_loc.language_code = ? " +
                    "WHERE movie_genre.id_movie = ?";
    private static final String SQL_ADD_LOCALIZED_GENRE_DATA =
            " INSERT INTO genre_localization(id_genre, language_code, name, description)" +
                    " VALUES (?,?,?,?)";
    private static final String SQL_UPDATE_LOCALIZED_GENRE_DATA =
            "UPDATE genre_localization SET name=?, description = ?" +
                    " WHERE id_genre = ? AND language_code = ?";
    private static final String SQL_CHECK_LOCALIZED_GENRE_INFO_BY_CODE =
            " SELECT * FROM genre_localization WHERE id_genre = ? AND language_code = ?";
    private static final String SQL_GET_LOCALIZED_GENRE_INFO_BY_CODE =
            " SELECT genre.id, g_loc.name, g_loc.description" +
                    " FROM genre INNER JOIN genre_localization AS g_loc" +
                    " ON genre.id = g_loc.id_genre" +
                    " AND language_code = ? WHERE genre.id = ?";

    @Override
    public int addGenre(Genre genre, String contentLanguage) throws DAOException {
        Connection connection = null;
        boolean isAdded;
        boolean isAddedId;
        int genreId = -1;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();

            connection.setAutoCommit(false);
            try (PreparedStatement addIdPreparedStatement =
                         connection.prepareStatement(SQL_ADD_GENRE,
                                 Statement.RETURN_GENERATED_KEYS)) {
                isAddedId = (addIdPreparedStatement.executeUpdate() == oneAffectedRow);
                if (isAddedId) {
                    genreId = returnGeneratedId(addIdPreparedStatement);
                } else {
                    throw new DAOException("Creating genre failed, no rows added.");
                }
            }
            try (PreparedStatement addLocalizedPreparedStatement =
                         connection.prepareStatement(SQL_ADD_LOCALIZED_GENRE_DATA)) {
                addLocalizedPreparedStatement.setInt(1, genreId);
                addLocalizedPreparedStatement.setString(2, contentLanguage);
                addLocalizedPreparedStatement.setString(3, genre.getName());
                addLocalizedPreparedStatement.setString(4, genre.getDescription());
                isAdded = (addLocalizedPreparedStatement.executeUpdate() == oneAffectedRow);
            }
            connection.commit();

        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_GENRE query", e);
        } finally {
            close(connection);
        }
        return genreId;
    }

    @Override
    public boolean addLocalizedGenreInfo(Genre genre, String contentLanguage) throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_LOCALIZED_GENRE_DATA);
            preparedStatement.setInt(1, genre.getId());
            preparedStatement.setString(2, contentLanguage);
            preparedStatement.setString(3, genre.getName());
            preparedStatement.setString(4, genre.getDescription());
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_LOCALIZED_GENRE_DATA query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isAdded;
    }

    @Override
    public boolean updateLocalizedGenreInfo(Genre genre, String contentLanguage)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_LOCALIZED_GENRE_DATA);
            preparedStatement.setString(1, genre.getName());
            preparedStatement.setString(2, genre.getDescription());
            preparedStatement.setInt(3, genre.getId());
            preparedStatement.setString(4, contentLanguage);
            isUpdated = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_LOCALIZED_GENRE_DATA query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isUpdated;
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

    @Override
    public boolean checkLocalizedGenreInfoByCode(int genreId, String languageCode)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isExist;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_CHECK_LOCALIZED_GENRE_INFO_BY_CODE);
            preparedStatement.setInt(1, genreId);
            preparedStatement.setString(2, languageCode);
            resultSet = preparedStatement.executeQuery();
            isExist = resultSet.next();
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_CHECK_LOCALIZED_GENRE_INFO_BY_CODE query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return isExist;
    }

    @Override
    public Genre getLocalizedGenreInfoByCode(int genreId, String languageCode)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Genre genre;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_LOCALIZED_GENRE_INFO_BY_CODE);
            preparedStatement.setString(1, languageCode);
            preparedStatement.setInt(2, genreId);
            resultSet = preparedStatement.executeQuery();
            genre = setDataForOneGenre(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_LOCALIZED_GENRE_INFO_BY_CODE query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return genre;
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
        genre.setDescription(isColumnExist(Column.DESCRIPTION, rs) ?
                rs.getString(Column.DESCRIPTION) : null);

        return genre;
    }

    private class Column {
        private static final String ID = "id";
        private static final String NAME = "name";
        private static final String DESCRIPTION = "description";
    }
}
