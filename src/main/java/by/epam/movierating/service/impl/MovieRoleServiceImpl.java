package by.epam.movierating.service.impl;

import by.epam.movierating.bean.MovieRole;
import by.epam.movierating.dao.MovieRoleDAO;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.factory.DAOFactory;
import by.epam.movierating.service.MovieRoleService;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;

/**
 * @author serge
 *         30.06.2017.
 */
public class MovieRoleServiceImpl implements MovieRoleService {

    @Override
    public List<MovieRole> getAllMovieRoles(String currentLanguage)
            throws ServiceException {
        //todo validate
        List<MovieRole> movieRoles;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieRoleDAO movieRoleDAO = daoFactory.getMovieRoleDAO();
            movieRoles = movieRoleDAO.getAllMovieRoles(currentLanguage);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting all movie roles", e);
        }
        return movieRoles;
    }
}
