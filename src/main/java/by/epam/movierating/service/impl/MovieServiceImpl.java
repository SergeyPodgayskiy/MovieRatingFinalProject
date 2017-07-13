package by.epam.movierating.service.impl;

import by.epam.movierating.bean.Movie;
import by.epam.movierating.dao.MovieDAO;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.factory.DAOFactory;
import by.epam.movierating.service.MovieService;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;

/**
 * @author serge
 *         23.06.2017.
 */
public class MovieServiceImpl implements MovieService {
    @Override
    public List<Movie> getTopMovies(String language) throws ServiceException {
        List<Movie> movieList;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            movieList = movieDAO.getTopMovies(language);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting list of TOP movies", e);
        }
        return movieList;
    }

    @Override
    public List<Movie> getMostDiscussedMovies(String language) throws ServiceException {
        List<Movie> movieList;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            movieList = movieDAO.getMostDiscussedMovies(language);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting list of Most Discussed movies", e);
        }
        return movieList;
    }

    @Override
    public Movie getMovieById(int idMovie, String language) throws ServiceException {
        Movie movie;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            movie = movieDAO.getMovieById(idMovie,language);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting Movie by Id", e);
        }
        return movie;
    }
}
