package by.epam.movierating.service;

import by.epam.movierating.bean.MovieRole;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;

/**
 * @author serge
 *         30.06.2017.
 */
public interface MovieRoleService {

    List<MovieRole> getAllMovieRoles(String currentLanguage)
            throws ServiceException;
}
