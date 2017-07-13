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
}
