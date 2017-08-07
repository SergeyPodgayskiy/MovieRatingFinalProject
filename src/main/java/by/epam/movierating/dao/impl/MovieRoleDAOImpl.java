package by.epam.movierating.dao.impl;

import by.epam.movierating.bean.MovieRole;
import by.epam.movierating.dao.MovieRoleDAO;
import by.epam.movierating.dao.connectionpool.ConnectionPool;
import by.epam.movierating.dao.exception.ConnectionPoolException;
import by.epam.movierating.dao.exception.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author serge
 *         11.06.2017.
 */
public class MovieRoleDAOImpl implements MovieRoleDAO {
    private static final String SQL_ADD_PARTICIPANT_ROLE =
            "INSERT INTO movierole (name) VALUES (?)";
    private static final String SQL_GET_ALL_MOVIE_ROLES =
            "SELECT movierole.id, movierole_localization.name" +
                    " FROM movierole" +
                    " INNER JOIN movierole_localization" +
                    " ON movierole.id = movierole_localization.id_movierole " +
                    " AND movierole_localization.language_code = ?";
    private static final String SQL_UPDATE_PARTICIPANT_ROLE =
            "UPDATE movierole SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_PARTICIPANT_ROLE =
            "UPDATE movierole SET deleted_at = ? WHERE id = ?";
    private static final String SQL_GET_MOVIE_ROLES_BY_PARTICIPANT_ID =
            "SELECT movierole.id, movierole_loc.name" +
                    " FROM movierole " +
                    " INNER JOIN movierole_localization AS movierole_loc" +
                    " ON movierole.id = movierole_loc.id_movierole" +
                    " INNER JOIN participant_movierole " +
                    " ON movierole.id = participant_movierole.id_movierole" +
                    " AND movierole_loc.language_code = ? " +
                    " WHERE participant_movierole.id_participant = ?";
    private static final String SQL_GET_ROLES_IN_MOVIE_BY_PARTICIPANT_ID =
            " SELECT movierole.id, movierole_loc.name" +
                    " FROM movierole " +
                    " INNER JOIN movierole_localization AS movierole_loc" +
                    " ON movierole.id = movierole_loc.id_movierole" +
                    " INNER JOIN movie_participant " +
                    " ON movierole.id = movie_participant.id_movierole" +
                    " AND movierole_loc.language_code = ? " +
                    " WHERE movie_participant.id_participant = ?" +
                    " AND movie_participant.id_movie = ?";

    @Override
    public int addParticipantMovieRole(MovieRole movieRole)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int idParticipantRole = -1;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_PARTICIPANT_ROLE,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, movieRole.getName());
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
            if (isAdded) {
                idParticipantRole = returnGeneratedId(preparedStatement);
            } else {
                throw new DAOException("Creating movie role failed, no rows affected.");
            }
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_PARTICIPANT_ROLE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return idParticipantRole;
    }

    @Override
    public List<MovieRole> getAllMovieRoles(String currentLanguage)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<MovieRole> movieRoleList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_MOVIE_ROLES);
            preparedStatement.setString(1, currentLanguage);
            resultSet = preparedStatement.executeQuery();
            movieRoleList = setDataForMovieRoles(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_ALL_MOVIE_ROLES query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return movieRoleList;
    }

    @Override
    public List<MovieRole> getRolesInMovieByParticipantId(int idParticipant, int idMovie, String language)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<MovieRole> movieRoleList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ROLES_IN_MOVIE_BY_PARTICIPANT_ID);
            preparedStatement.setString(1, language);
            preparedStatement.setInt(2, idParticipant);
            preparedStatement.setInt(3, idMovie);
            resultSet = preparedStatement.executeQuery();
            movieRoleList = setDataForMovieRoles(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_ROLES_IN_MOVIE_BY_PARTICIPANT_ID query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return movieRoleList;
    }

    @Override
    public List<MovieRole> getMovieRolesByParticipantId(int idParticipant, String language)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<MovieRole> movieRoles;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_MOVIE_ROLES_BY_PARTICIPANT_ID);
            preparedStatement.setString(1, language);
            preparedStatement.setInt(2, idParticipant);
            resultSet = preparedStatement.executeQuery();
            movieRoles = setDataForMovieRoles(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_MOVIE_ROLES_BY_PARTICIPANT_ID query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return movieRoles;
    }

    @Override
    public boolean updateParticipantMovieRole(MovieRole movieRole)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_PARTICIPANT_ROLE);
            preparedStatement.setString(1, movieRole.getName());
            preparedStatement.setInt(2, movieRole.getId());
            isUpdated = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_PARTICIPANT_ROLE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteParticipantMovieRole(int idParticipantMovieRole)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isDeleted;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_PARTICIPANT_ROLE);
            preparedStatement.setInt(1, idParticipantMovieRole);
            isDeleted = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_DELETE_PARTICIPANT_ROLE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isDeleted;
    }

    private MovieRole setDataForOneMovieRole(ResultSet resultSet)
            throws SQLException {
        MovieRole movieRole = null;
        if (resultSet.next()) {
            movieRole = createParticipantMovieRole(resultSet);
        }
        return movieRole;
    }

    private List<MovieRole> setDataForMovieRoles(ResultSet resultSet)
            throws SQLException {
        List<MovieRole> movieRoleList = new ArrayList<>();
        while (resultSet.next()) {
            MovieRole movieRole
                    = createParticipantMovieRole(resultSet);
            movieRoleList.add(movieRole);
        }
        return movieRoleList;
    }

    private MovieRole createParticipantMovieRole(ResultSet rs) throws SQLException {
        MovieRole movieRole = new MovieRole();

        movieRole.setId(isColumnExist(Column.ID, rs) ?
                rs.getInt(Column.ID) : -1);
        movieRole.setName(isColumnExist(Column.NAME, rs) ?
                rs.getString(Column.NAME) : null);

        return movieRole;
    }

    private class Column {
        private static final String ID = "id";
        private static final String NAME = "name";
    }

}
