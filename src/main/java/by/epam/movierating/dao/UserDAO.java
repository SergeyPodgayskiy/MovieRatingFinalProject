package by.epam.movierating.dao;

import by.epam.movierating.bean.User;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.util.ColumnExistable;
import by.epam.movierating.dao.util.DAODefaultFunctional;
import by.epam.movierating.dao.util.JDBCAutocloseable;

import java.util.List;

/**
 * @author serge
 *         08.05.2017.
 */
public interface UserDAO extends DAODefaultFunctional, JDBCAutocloseable, ColumnExistable {

    int register(User user) throws DAOException;

    User getUserByLogin(String login) throws DAOException;

    User getUserById(int idUser) throws DAOException;

    List<User> getAllUsers() throws DAOException;

    boolean updateUser(User user) throws DAOException;

    boolean deleteUser(int idUser) throws DAOException;

    boolean updateAdminStatus(int idUser, boolean adminStatus) throws DAOException;

    boolean updateBanStatus(int idUser, boolean banStatus) throws DAOException;
}
