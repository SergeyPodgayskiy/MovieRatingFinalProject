package by.epam.movierating.dao.impl;

import by.epam.movierating.bean.Country;
import by.epam.movierating.bean.User;
import by.epam.movierating.dao.UserDAO;
import by.epam.movierating.dao.connectionpool.ConnectionPool;
import by.epam.movierating.dao.exception.ConnectionPoolException;
import by.epam.movierating.dao.exception.DAOException;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @author serge
 *         08.05.2017.
 */
public class UserDAOImpl implements UserDAO {

    private static final String GET_USER_BY_LOGIN =
            "SELECT * FROM `user` WHERE login=? AND `user`.deleted_at IS NULL ";
    private static final String SQL_REGISTER =
            "INSERT INTO `user`(login, password, email, register_date,full_name)" +
                    " VALUES(?,?,?,?,?)";
    private static final String SQL_GET_USER_BY_ID =
            "SELECT id,country_code,login,email,is_admin,is_banned,register_date,full_name,birth_date," +
                    " photo_url,amount_of_marks, amount_of_reviews, deleted_at" +
                    " FROM `user`" +
                    " WHERE id=?";
    private static final String SQL_GET_ALL_USERS =
            "SELECT user.id, user.login, user.is_admin, user.is_banned, user.deleted_at, " +
                    "user.full_name, user.photo_url, user.register_date" +
                    " FROM `user`" +
                    "WHERE user.deleted_at IS NULL ";
    private static final String SQL_UPDATE_USER =
            "UPDATE `user` SET login=?, email=?,  is_admin=?, is_banned=?, register_date=?, " +
                    "WHERE id=?";
    private static final String SQL_DELETE_USER =
            "UPDATE `user` SET user.deleted_at=? WHERE user.id=?";
    private static final String SQL_UPDATE_ADMIN_STATUS =
            "UPDATE user SET user.is_admin = ? WHERE user.id = ?";
    private static final String SQL_UPDATE_BAN_STATUS =
            "UPDATE user SET user.is_banned = ? WHERE user.id = ?";

    @Override
    public int register(User user) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isRegistered;
        int userId = -1;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_REGISTER,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setDate(4,
                    convertJavaDateToSqlDate(user.getRegisterDate()));
            preparedStatement.setString(5, user.getFullName());
            isRegistered = (preparedStatement.executeUpdate() == oneAffectedRow);
            if (isRegistered) {
                userId = returnGeneratedId(preparedStatement);
            } else {
                throw new DAOException("Creating user failed, no rows affected.");
            }
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_REGISTER query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return userId;
    }

    @Override
    public User getUserByLogin(String login) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(GET_USER_BY_LOGIN);
            preparedStatement.setString(1, login);
            resultSet = preparedStatement.executeQuery();
            user = setDataForOneUser(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can't get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during GET_USER_BY_LOGIN query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return user;
    }

    @Override
    public User getUserById(int idUser) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_USER_BY_ID);
            preparedStatement.setInt(1, idUser);
            resultSet = preparedStatement.executeQuery();
            user = setDataForOneUser(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_USER_BY_ID query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> userList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_USERS);
            resultSet = preparedStatement.executeQuery();
            userList = setDataForUsers(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_ALL_USERS query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return userList;
    }

    @Override
    public boolean updateUser(User user) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_USER);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setBoolean(3, user.isAdmin());
            preparedStatement.setBoolean(4, user.isBanned());
            preparedStatement.setDate(5,
                    convertJavaDateToSqlDate(user.getRegisterDate()));
            preparedStatement.setInt(6, user.getId());
            isUpdated = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_USER query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteUser(int idUser) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isDeleted;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_USER);
            preparedStatement.setDate(1, convertJavaDateToSqlDate(new Date()));
            preparedStatement.setInt(2, idUser);
            isDeleted = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_DELETE_USER query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isDeleted;
    }

    @Override
    public boolean updateBanStatus(int idUser, boolean banStatus) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_BAN_STATUS);
            preparedStatement.setBoolean(1, banStatus);
            preparedStatement.setInt(2, idUser);
            isUpdated = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_BAN_STATUS query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isUpdated;
    }

    @Override
    public boolean updateAdminStatus(int idUser, boolean adminStatus) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_ADMIN_STATUS);
            preparedStatement.setBoolean(1, adminStatus);
            preparedStatement.setInt(2, idUser);
            isUpdated = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_ADMIN_STATUS query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isUpdated;
    }

    private User setDataForOneUser(ResultSet resultSet) throws SQLException {
        User user = null;
        if (resultSet.next()) {
            user = createUser(resultSet);
        }
        return user;
    }

    private List<User> setDataForUsers(ResultSet resultSet) throws SQLException {
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = createUser(resultSet);
            userList.add(user);
        }
        return userList;
    }

    private User createUser(ResultSet rs) throws SQLException {
        User user = new User();

        user.setId(rs.getInt(Column.ID));
        if (isColumnExist(Column.COUNTRY_CODE, rs)) {
            Country country = new Country();
            country.setCode(rs.getString(Column.COUNTRY_CODE));
            user.setCountry(country);
        }
        user.setLogin(isColumnExist(Column.LOGIN, rs) ?
                rs.getString(Column.LOGIN) : null);
        user.setPassword(isColumnExist(Column.PASSWORD, rs) ?
                rs.getString(Column.PASSWORD) : null);
        user.setEmail(isColumnExist(Column.EMAIL, rs) ?
                rs.getString(Column.EMAIL) : null);
        user.setAdmin(isColumnExist(Column.IS_ADMIN, rs) &&
                rs.getBoolean(Column.IS_ADMIN));
        user.setBanned(isColumnExist(Column.IS_BANNED, rs) &&
                rs.getBoolean(Column.IS_BANNED));
        user.setRegisterDate(isColumnExist(Column.REGISTER_DATE, rs) ?
                rs.getDate(Column.REGISTER_DATE) : null);
        user.setFullName(isColumnExist(Column.FULL_NAME, rs) ?
                rs.getString(Column.FULL_NAME) : null);
        user.setBirthDate(isColumnExist(Column.BIRTH_DATE, rs) ?
                rs.getDate(Column.BIRTH_DATE) : null);
        user.setPhotoURL(isColumnExist(Column.PHOTO_URL, rs) ?
                rs.getString(Column.PHOTO_URL) : null);
        user.setAmountOfMarks(isColumnExist(Column.AMOUNT_OF_MARKS, rs) ?
                rs.getInt(Column.AMOUNT_OF_MARKS) : 0);
        user.setAmountOfReviews(isColumnExist(Column.AMOUNT_OF_REVIEWS, rs) ?
                rs.getInt(Column.AMOUNT_OF_REVIEWS) : 0);
        user.setDeletedAt(isColumnExist(Column.DELETED_AT, rs) ?
                rs.getDate(Column.DELETED_AT) : null);

        return user;
    }

    private class Column {
        private static final String ID = "id";
        private static final String COUNTRY_CODE = "country_code";
        private static final String LOGIN = "login";
        private static final String PASSWORD = "password";
        private static final String EMAIL = "email";
        private static final String IS_ADMIN = "is_admin";
        private static final String IS_BANNED = "is_banned";
        private static final String REGISTER_DATE = "register_date";
        private static final String BIRTH_DATE = "birth_date";
        private static final String FULL_NAME = "full_name";
        private static final String PHOTO_URL = "photo_url";
        private static final String AMOUNT_OF_MARKS = "amount_of_marks";
        private static final String AMOUNT_OF_REVIEWS = "amount_of_reviews";
        private static final String DELETED_AT = "deleted_at";
    }
}
