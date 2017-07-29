package by.epam.movierating.service;

import by.epam.movierating.bean.User;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;

/**
 * @author serge
 *         09.05.2017.
 */
public interface UserService {

    User register(String login, byte[] password, byte[] confirmedPassword, String fullName, String email)
            throws ServiceException;

    User logIn(String login, byte[] password) throws ServiceException;

    List<User> getAllUser() throws ServiceException;

    User getUserById(int idUser) throws ServiceException;

    boolean updateAdminStatus(int idUser, boolean currentAdminStatus) throws ServiceException;

    boolean updateBanStatus(int idUser, boolean currentBanStatus) throws ServiceException;

    boolean deleteUserById(int idUser) throws ServiceException;
}
