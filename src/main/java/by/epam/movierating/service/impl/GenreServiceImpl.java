package by.epam.movierating.service.impl;

import by.epam.movierating.bean.Genre;
import by.epam.movierating.dao.GenreDAO;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.factory.DAOFactory;
import by.epam.movierating.service.GenreService;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;

/**
 * @author serge
 *         30.06.2017.
 */
public class GenreServiceImpl implements GenreService {
    @Override
    public List<Genre> getGenresByMovieId(int idMovie, String language) throws ServiceException {
        List<Genre> genreList;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            GenreDAO genreDAO = daoFactory.getGenreDAO();
            genreList = genreDAO.getGenresByMovieId(idMovie,language);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting list of genres by movieId", e);
        }
        return genreList;
    }
}
