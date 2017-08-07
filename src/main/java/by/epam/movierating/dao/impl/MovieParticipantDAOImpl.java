package by.epam.movierating.dao.impl;

import by.epam.movierating.bean.Country;
import by.epam.movierating.bean.MovieParticipant;
import by.epam.movierating.dao.MovieParticipantDAO;
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
public class MovieParticipantDAOImpl implements MovieParticipantDAO {
    private static final String SQL_ADD_PARTICIPANT =
            "INSERT INTO participant (birth_date) VALUES (?)";
    private static final String SQL_GET_PARTICIPANT_BY_ID =
            "SELECT participant.*, participant_localization.name, participant_localization.surname" +
                    " FROM participant INNER JOIN participant_localization ON" +
                    " participant.id = participant_localization.id_participant" +
                    " AND participant_localization.language_code = ?" +
                    " WHERE participant.id=?";
    private static final String SQL_GET_PARTICIPANTS_BY_MOVIE_ID =
            "SELECT DISTINCT participant.*, participant_loc.name, participant_loc.surname " +
                    " FROM participant" +
                    " INNER JOIN participant_localization AS participant_loc " +
                    " ON participant.id = participant_loc.id_participant" +
                    " INNER JOIN movie_participant" +
                    " ON participant.id = movie_participant.id_participant " +
                    " AND participant_loc.language_code = ?" +
                    " WHERE movie_participant.id_movie = ?" +
                    " ORDER BY participant_loc.name ";
    private static final String SQL_GET_ALL_PARTICIPANTS =
            "SELECT * FROM participant";
    private static final String SQL_GET_ALL_PARTICIPANTS_BY_ID_ROLE =
            "SELECT participant.*, participant_loc.name, participant_loc.surname " +
                    " FROM participant" +
                    " INNER JOIN participant_localization AS participant_loc " +
                    " ON participant.id = participant_loc.id_participant" +
                    " AND participant_loc.language_code = ?" +
                    " INNER JOIN participant_movierole " +
                    " ON participant.id = participant_movierole.id_participant" +
                    " WHERE participant_movierole.id_movierole = ?" +
                    " ORDER BY participant_loc.name ";

    private static final String SQL_UPDATE_PARTICIPANT =
            "UPDATE participant" +
                    " SET birth_date = ? WHERE participant.id = ?";
    private static final String SQL_DELETE_PARTICIPANT =
            "UPDATE participant SET deleted_at = ? WHERE id = ?";
    private static final String SQL_UPDATE_PARTICIPANT_PHOTO =
            "UPDATE participant SET photo_url = ? WHERE id = ?";
    private static final String SQL_GET_PARTICIPANTS_IN_MOVIE_BY_ID_ROLE =
            "SELECT participant.*, participant_loc.name, participant_loc.surname " +
                    " FROM participant" +
                    " INNER JOIN participant_localization AS participant_loc " +
                    " ON participant.id = participant_loc.id_participant" +
                    " AND participant_loc.language_code = ?" +
                    " INNER JOIN movie_participant " +
                    " ON participant.id = movie_participant.id_participant" +
                    " WHERE movie_participant.id_movierole = ?" +
                    " AND movie_participant.id_movie = ?" +
                    " ORDER BY participant_loc.name ";
    private static final String SQL_CHECK_LANGUAGE_PARTICIPANT_INFO_BY_CODE =
            "SELECT * FROM participant_localization WHERE id_participant = ? AND language_code = ?";
    private static final String SQL_GET_LANGUAGE_PARTICIPANT_INFO_BY_CODE =
            " SELECT participant.*,p_loc.name,p_loc.surname " +
                    " FROM participant INNER JOIN participant_localization AS p_loc" +
                    " ON participant.id = p_loc.id_participant AND" +
                    " language_code = ? WHERE id_participant = ?";
    private static final String SQL_ADD_LANGUAGE_DEPENDENT_PARTICIPANT_INFO =
            " INSERT INTO participant_localization(id_participant, language_code, name, surname)" +
                    " VALUES (?,?,?,?)";
    private static final String SQL_UPDATE_LANGUAGE_DEPENDENT_PARTICIPANT_INFO =
            " UPDATE participant_localization SET name = ?, surname = ?" +
                    " WHERE id_participant = ? AND language_code = ?";
    private static final String SQL_ADD_COUNTRY_FOR_PARTICIPANT =
            " UPDATE participant SET country_code = ? WHERE id = ?";
    private static final String SQL_ADD_ROLE_FOR_PARTICIPANT =
            " INSERT INTO participant_movierole(id_participant, id_movierole) VALUES (?,?)";
    private static final String SQL_DELETE_ROLE_FOR_PARTICIPANT =
            " DELETE FROM participant_movierole WHERE id_participant = ? AND id_movierole = ?";
    private static final String SQL_GET_ALL_LIMITED_ACTORS =
            " SELECT participant.*, p_loc.name, p_loc.surname" +
                    " FROM participant" +
                    " INNER JOIN participant_localization AS p_loc" +
                    " ON participant.id = p_loc.id_participant " +
                    " INNER JOIN participant_movierole" +
                    " ON participant.id=participant_movierole.id_participant" +
                    " AND p_loc.language_code = ?" +
                    " WHERE participant_movierole.id_movierole = ? AND participant.deleted_at IS NULL " +
                    "ORDER BY p_loc.name, p_loc.surname" +
                    " LIMIT ?,?";
    private static final int ID_ROLE_ACTOR = 3;
    private static final int LIMIT_ACTOR_COUNT = 5;

    @Override
    public int addMovieParticipant(MovieParticipant movieParticipant) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int participantId = -1;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_PARTICIPANT,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, convertJavaDateToSqlDate(movieParticipant.getBirthDate()));
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
            if (isAdded) {
                participantId = returnGeneratedId(preparedStatement);
            } else {
                throw new DAOException("Creating movie participant failed, no rows added.");
            }
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_PARTICIPANT query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return participantId;
    }

    @Override
    public boolean addLocalizedParticipantInfo(MovieParticipant participant,
                                               String contentLanguage)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_LANGUAGE_DEPENDENT_PARTICIPANT_INFO);
            preparedStatement.setInt(1, participant.getId());
            preparedStatement.setString(2, contentLanguage);
            preparedStatement.setString(3, participant.getName());
            preparedStatement.setString(4, participant.getSurname());
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_LANGUAGE_DEPENDENT_PARTICIPANT_INFO query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isAdded;
    }

    @Override
    public boolean addCountryForParticipant(int participantId, String countryCode)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_COUNTRY_FOR_PARTICIPANT);
            preparedStatement.setString(1, countryCode);
            preparedStatement.setInt(2, participantId);
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_COUNTRY_FOR_PARTICIPANT query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isAdded;
    }

    @Override
    public boolean addRoleForParticipant(int participantId, int roleId) throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_ROLE_FOR_PARTICIPANT);
            preparedStatement.setInt(1, participantId);
            preparedStatement.setInt(2, roleId);
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_ROLE_FOR_PARTICIPANT query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isAdded;
    }

    @Override
    public boolean deleteRoleForParticipant(int participantId, int roleId)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isDeleted;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_ROLE_FOR_PARTICIPANT);
            preparedStatement.setInt(1, participantId);
            preparedStatement.setInt(2, roleId);
            isDeleted = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_DELETE_ROLE_FOR_PARTICIPANT query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isDeleted;
    }

    @Override
    public boolean updateLocalizedParticipantInfo(MovieParticipant participant,
                                                  String contentLanguage) throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_LANGUAGE_DEPENDENT_PARTICIPANT_INFO);
            preparedStatement.setString(1, participant.getName());
            preparedStatement.setString(2, participant.getSurname());
            preparedStatement.setInt(3, participant.getId());
            preparedStatement.setString(4, contentLanguage);
            isUpdated = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_LANGUAGE_DEPENDENT_PARTICIPANT_INFO query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isUpdated;
    }

    @Override
    public MovieParticipant getMovieParticipantById(int idParticipant, String language)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        MovieParticipant movieParticipant = null;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_PARTICIPANT_BY_ID);
            preparedStatement.setString(1, language);
            preparedStatement.setInt(2, idParticipant);
            resultSet = preparedStatement.executeQuery();
            movieParticipant = setDataForOneParticipant(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_PARTICIPANT_BY_ID query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return movieParticipant;
    }

    @Override
    public List<MovieParticipant> getMovieParticipantsByMovieId(int idMovie, String language)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<MovieParticipant> participantsList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_PARTICIPANTS_BY_MOVIE_ID);
            preparedStatement.setString(1, language);
            preparedStatement.setInt(2, idMovie);
            resultSet = preparedStatement.executeQuery();
            participantsList = setDataForParticipants(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_PARTICIPANTS_BY_MOVIE_ID query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return participantsList;
    }

    @Override
    public List<MovieParticipant> getAllMovieParticipants() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<MovieParticipant> participantsList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_PARTICIPANTS);
            resultSet = preparedStatement.executeQuery();
            participantsList = setDataForParticipants(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_ALL_PARTICIPANTS query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return participantsList;
    }


    @Override
    public List<MovieParticipant> getAllParticipantsByRoleId(int roleId, String language)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<MovieParticipant> participantList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_PARTICIPANTS_BY_ID_ROLE);
            preparedStatement.setString(1, language);
            preparedStatement.setInt(2, roleId);
            resultSet = preparedStatement.executeQuery();
            participantList = setDataForParticipants(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_ALL_PARTICIPANTS_BY_ID_ROLE  query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return participantList;
    }

    @Override
    public boolean updateMovieParticipant(MovieParticipant movieParticipant)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_PARTICIPANT);
            preparedStatement.setDate(1,
                    convertJavaDateToSqlDate(movieParticipant.getBirthDate()));
            preparedStatement.setInt(2, movieParticipant.getId());
            isUpdated = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_PARTICIPANT query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteMovieParticipant(int idParticipant) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isDeleted;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_PARTICIPANT);
            preparedStatement.setInt(1, idParticipant);
            isDeleted = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_DELETE_PARTICIPANT query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isDeleted;
    }

    @Override
    public boolean uploadMovieParticipantPhoto(int idParticipant, String photoURL) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isPhotoUploaded;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_PARTICIPANT_PHOTO);
            preparedStatement.setString(1, photoURL);
            preparedStatement.setInt(2, idParticipant);
            isPhotoUploaded = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_PARTICIPANT_PHOTO query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isPhotoUploaded;
    }

    @Override
    public List<MovieParticipant> getParticipantsInMovieByRole(int movieId,
                                                               int roleId,
                                                               String currentLanguage)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<MovieParticipant> participantList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_PARTICIPANTS_IN_MOVIE_BY_ID_ROLE);
            preparedStatement.setString(1, currentLanguage);
            preparedStatement.setInt(2, roleId);
            preparedStatement.setInt(3, movieId);
            resultSet = preparedStatement.executeQuery();
            participantList = setDataForParticipants(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_PARTICIPANTS_IN_MOVIE_BY_ID_ROLE query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return participantList;
    }

    @Override
    public boolean checkLocalizedParticipantInfo(int participantId, String languageCode)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isExist;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_CHECK_LANGUAGE_PARTICIPANT_INFO_BY_CODE);
            preparedStatement.setInt(1, participantId);
            preparedStatement.setString(2, languageCode);
            resultSet = preparedStatement.executeQuery();
            isExist = resultSet.next();
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_CHECK_LANGUAGE_PARTICIPANT_INFO_BY_CODE query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return isExist;
    }

    @Override
    public MovieParticipant getLocalizedParticipantInfo(int participantId, String languageCode)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        MovieParticipant participant;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_LANGUAGE_PARTICIPANT_INFO_BY_CODE);
            preparedStatement.setString(1, languageCode);
            preparedStatement.setInt(2, participantId);
            resultSet = preparedStatement.executeQuery();
            participant = setDataForOneParticipant(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_LANGUAGE_PARTICIPANT_INFO_BY_CODE query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return participant;
    }

    @Override
    public List<MovieParticipant> getAllLimitedActors(String currentLanguage, int currentPageNumber)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<MovieParticipant> participantsList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_LIMITED_ACTORS);
            preparedStatement.setString(1, currentLanguage);
            preparedStatement.setInt(2, ID_ROLE_ACTOR);
            preparedStatement.setInt(3, LIMIT_ACTOR_COUNT * (currentPageNumber - 1));
            preparedStatement.setInt(4, LIMIT_ACTOR_COUNT);
            resultSet = preparedStatement.executeQuery();
            participantsList = setDataForParticipants(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_ALL_LIMITED_ACTORS query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return participantsList;
    }

    private MovieParticipant setDataForOneParticipant(ResultSet resultSet)
            throws SQLException {
        MovieParticipant movieParticipant = null;
        if (resultSet.next()) {
            movieParticipant = createMovieParticipant(resultSet);
        }
        return movieParticipant;
    }

    private List<MovieParticipant> setDataForParticipants(ResultSet resultSet)
            throws SQLException {
        List<MovieParticipant> participantList = new ArrayList<>();
        while (resultSet.next()) {
            MovieParticipant movieParticipant = createMovieParticipant(resultSet);
            participantList.add(movieParticipant);
        }
        return participantList;
    }

    private MovieParticipant createMovieParticipant(ResultSet rs) throws SQLException {
        MovieParticipant movieParticipant = new MovieParticipant();

        movieParticipant.setId(isColumnExist(Column.ID, rs) ?
                rs.getInt(Column.ID) : -1);
        if (isColumnExist(Column.COUNTRY_CODE, rs)) {
            Country country = new Country();
            country.setCode(rs.getString(Column.COUNTRY_CODE));
            movieParticipant.setCountry(country);
        }
        movieParticipant.setBirthDate(isColumnExist(Column.BIRTH_DATE, rs) ?
                rs.getDate(Column.BIRTH_DATE) : null);
        movieParticipant.setPhotoURL(isColumnExist(Column.PHOTO_URL, rs) ?
                rs.getString(Column.PHOTO_URL) : null);
        movieParticipant.setAmountOfMovies(isColumnExist(Column.AMOUNT_OF_MOVIES, rs) ?
                rs.getInt(Column.AMOUNT_OF_MOVIES) : -1);
        movieParticipant.setDeletedAt(isColumnExist(Column.DELETED_AT, rs) ?
                rs.getDate(Column.DELETED_AT) : null);
        movieParticipant.setName(isColumnExist(Column.NAME, rs) ?
                rs.getString(Column.NAME) : null);
        movieParticipant.setSurname(isColumnExist(Column.SURNAME, rs) ?
                rs.getString(Column.SURNAME) : null);
        return movieParticipant;
    }

    private class Column {
        private static final String ID = "id";
        private static final String COUNTRY_CODE = "country_code";
        private static final String BIRTH_DATE = "birth_date";
        private static final String PHOTO_URL = "photo_url";
        private static final String AMOUNT_OF_MOVIES = "amount_of_movies";
        private static final String DELETED_AT = "deleted_at";
        private static final String NAME = "name";
        private static final String SURNAME = "surname";
    }
}
