package by.epam.movierating.dao.impl;

import by.epam.movierating.bean.ParticipantMovieRole;
import by.epam.movierating.dao.ParticipantMovieRoleDAO;
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
public class ParticipantMovieRoleDAOImpl implements ParticipantMovieRoleDAO {
    private static final String SQL_ADD_PARTICIPANT_ROLE =
            "INSERT INTO movierole (name) VALUES (?)";
    private static final String SQL_GET_ALL_PARTICIPANT_ROLES =
            "SELECT * FROM movierole";
    private static final String SQL_GET_PARTICIPANT_ROLE_BY_ID =
            "SELECT * FROM movierole WHERE id = ?";
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

    @Override
    public int addParticipantMovieRole(ParticipantMovieRole participantMovieRole)
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
            preparedStatement.setString(1, participantMovieRole.getName());
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
    public List<ParticipantMovieRole> getAllParticipantMovieRoles()
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<ParticipantMovieRole> movieRoleList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_PARTICIPANT_ROLES);
            resultSet = preparedStatement.executeQuery();
            movieRoleList = setDataForMovieRoles(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_ALL_PARTICIPANT_ROLES query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return movieRoleList;
    }

    @Override
    public List<ParticipantMovieRole> getMovieRolesByParticipantId(int idParticipant, String language)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<ParticipantMovieRole> participantMovieRoleList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_MOVIE_ROLES_BY_PARTICIPANT_ID);
            preparedStatement.setString(1, language);
            preparedStatement.setInt(2, idParticipant);
            resultSet = preparedStatement.executeQuery();
            participantMovieRoleList = setDataForMovieRoles(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_MOVIE_ROLES_BY_PARTICIPANT_ID query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return participantMovieRoleList;
    }

    @Override
    public ParticipantMovieRole getParticipantMovieRoleById(int idParticipantMovieRole) //// TODO: 01.07.2017 may be useless
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ParticipantMovieRole participantMovieRole;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_PARTICIPANT_ROLE_BY_ID);
            preparedStatement.setInt(1, idParticipantMovieRole);
            resultSet = preparedStatement.executeQuery();
            participantMovieRole = setDataForOneMovieRole(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_PARTICIPANT_ROLE_BY_ID query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return participantMovieRole;
    }

    @Override
    public boolean updateParticipantMovieRole(ParticipantMovieRole participantMovieRole)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_PARTICIPANT_ROLE);
            preparedStatement.setString(1, participantMovieRole.getName());
            preparedStatement.setInt(2, participantMovieRole.getId());
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

    private ParticipantMovieRole setDataForOneMovieRole(ResultSet resultSet)
            throws SQLException {
        ParticipantMovieRole movieRole = null;
        if (resultSet.next()) {
            movieRole = createParticipantMovieRole(resultSet);
        }
        return movieRole;
    }

    private List<ParticipantMovieRole> setDataForMovieRoles(ResultSet resultSet)
            throws SQLException {
        List<ParticipantMovieRole> movieRoleList = new ArrayList<>();
        while (resultSet.next()) {
            ParticipantMovieRole movieRole
                    = createParticipantMovieRole(resultSet);
            movieRoleList.add(movieRole);
        }
        return movieRoleList;
    }

    private ParticipantMovieRole createParticipantMovieRole(ResultSet resultSet)
            throws SQLException {
        ParticipantMovieRole movieRole = new ParticipantMovieRole();
        movieRole.setId(resultSet.getInt(1));
        movieRole.setName(resultSet.getString(2));
        return movieRole;
    }
}
