package by.epam.movierating.dao.impl;

import by.epam.movierating.bean.Country;
import by.epam.movierating.bean.User;
import by.epam.movierating.dao.UserDAO;
import by.epam.movierating.dao.connectionpool.ConnectionPool;
import by.epam.movierating.dao.exception.ConnectionPoolException;
import by.epam.movierating.dao.exception.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author serge
 *         08.05.2017.
 */
public class UserDAOImpl implements UserDAO {

    private static final String GET_USER_BY_LOGIN =
            "SELECT * FROM `user` WHERE login=?";
    private static final String SQL_REGISTER =
            "INSERT INTO `user`(login, password, email, register_date,full_name)" +
                    " VALUES(?,?,?,?,?)";
    private static final String SQL_GET_USER_BY_ID =
            "SELECT * FROM `user` WHERE id=?";
    private static final String SQL_GET_ALL_USERS =
            "SELECT user.id, user.login, user.is_admin, user.is_banned, user.deleted_at, " +
                    "user.full_name, user.photo_url, user.register_date" +
                    " FROM `user`";
    private static final String SQL_UPDATE_USER =
            "UPDATE `user` SET login=?, email=?,  is_admin=?, is_banned=?, register_date=?, " +
                    "WHERE id=?";
    private static final String SQL_DELETE_USER =
            "UPDATE `user` SET deleted_at=? WHERE id=?";
    private static final String SQL_BAN_USER =
            "UPDATE `user` SET is_banned=1 WHERE id=?";
    private static final String SQL_UNBAN_USER =
            "UPDATE `user` SET is_banned=0 WHERE id=?";

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
            preparedStatement.setInt(1, idUser);
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
    public boolean banUser(int idUser) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isBanned;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_BAN_USER);
            preparedStatement.setInt(1, idUser);
            isBanned = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_BAN_USER query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isBanned;
    }

    @Override
    public boolean unbanUser(int idUser) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUnbanned;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UNBAN_USER);
            preparedStatement.setInt(1, idUser);
            isUnbanned = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UNBAN_USER query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isUnbanned;
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
        user.setId(rs.getInt(Column.ID.toString()));
        Country country = new Country();
        country.setCode(isColumnExist(Column.COUNTRY_CODE.toString(), rs) ?
                rs.getString(Column.COUNTRY_CODE.toString()) : null);
        user.setCountry(country);
        user.setLogin(isColumnExist(Column.LOGIN.toString(), rs) ?
                rs.getString(Column.LOGIN.toString()) : null);
        user.setPassword(isColumnExist(Column.PASSWORD.toString(), rs) ?
                rs.getString(Column.PASSWORD.toString()) : null);
        user.setEmail(isColumnExist(Column.EMAIL.toString(), rs) ?
                rs.getString(Column.EMAIL.toString()) : null);
        user.setAdmin(isColumnExist(Column.IS_ADMIN.toString(), rs) &&
                rs.getBoolean(Column.IS_ADMIN.toString()));
        user.setBanned(isColumnExist(Column.IS_BANNED.toString(), rs) &&
                rs.getBoolean(Column.IS_BANNED.toString()));
        user.setRegisterDate(isColumnExist(Column.REGISTER_DATE.toString(), rs) ?
                rs.getDate(Column.REGISTER_DATE.toString()) : null);
        user.setFullName(isColumnExist(Column.FULL_NAME.toString(), rs) ?
                rs.getString(Column.FULL_NAME.toString()) : null);
        user.setBirthDate(isColumnExist(Column.BIRTH_DATE.toString(), rs) ?
                rs.getDate(Column.BIRTH_DATE.toString()) : null);
        user.setPhotoURL(isColumnExist(Column.PHOTO_URL.toString(), rs) ?
                rs.getString(Column.PHOTO_URL.toString()) : null);
        user.setAmountOfMarks(isColumnExist(Column.AMOUNT_OF_MARKS.toString(), rs) ?
                rs.getInt(Column.AMOUNT_OF_MARKS.toString()) : 0);
        user.setAmountOfReviews(isColumnExist(Column.AMOUNT_OF_REVIEWS.toString(), rs) ?
               rs.getInt(Column.AMOUNT_OF_REVIEWS.toString()) : 0);
        user.setDeletedAt(isColumnExist(Column.DELETED_AT.toString(),rs) ?
                rs.getDate(Column.DELETED_AT.toString()) : null);

        return user;
    }

    private static boolean isColumnExist(String columnName, ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int numCol = meta.getColumnCount();
        for (int i = 1; i <= numCol; i++) {
            if (meta.getColumnName(i).equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }

    private enum Column {
        ID, COUNTRY_CODE, LOGIN, PASSWORD, EMAIL, IS_ADMIN,
        IS_BANNED, REGISTER_DATE, FULL_NAME, BIRTH_DATE, PHOTO_URL,
        AMOUNT_OF_MARKS, AMOUNT_OF_REVIEWS, DELETED_AT;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
