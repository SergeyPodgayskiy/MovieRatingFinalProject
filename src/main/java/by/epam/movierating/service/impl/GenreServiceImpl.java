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
            genreList = genreDAO.getGenresByMovieId(idMovie, language);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting list of genres by movieId", e);
        }
        return genreList;
    }

    @Override
    public List<Genre> getAllGenres(String language) throws ServiceException {
        List<Genre> genreList;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            GenreDAO genreDAO = daoFactory.getGenreDAO();
            genreList = genreDAO.getAllGenres(language);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting list of all genres", e);
        }
        return genreList;
    }

    @Override
    public int addGenre(String name, String description, String contentLanguage) throws ServiceException {
        int genreId;

        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            GenreDAO genreDAO = daoFactory.getGenreDAO();

            Genre genre = new Genre();
            genre.setName(name);
            genre.setDescription(description);

            genreId = genreDAO.addGenre(genre, contentLanguage);
        } catch (DAOException e) {
            throw new ServiceException("Error during adding genre", e);
        }
        return genreId;
    }

    @Override
    public boolean addLocalizedGenreInfo(int genreId,
                                         String name,
                                         String description,
                                         String contentLanguage)
            throws ServiceException {

        boolean isAdded;

        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            GenreDAO genreDAO = daoFactory.getGenreDAO();

            Genre genre = new Genre();
            genre.setId(genreId);
            genre.setName(name);
            genre.setDescription(description);

            isAdded = genreDAO.addLocalizedGenreInfo(genre, contentLanguage);
        } catch (DAOException e) {
            throw new ServiceException("Error during adding localized genre info", e);
        }
        return isAdded;
    }

    @Override
    public boolean updateLocalizedGenreInfo(int genreId,
                                            String name,
                                            String description,
                                            String contentLanguage)
            throws ServiceException {

        boolean isUpdated;

        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            GenreDAO genreDAO = daoFactory.getGenreDAO();

            Genre genre = new Genre();
            genre.setId(genreId);
            genre.setName(name);
            genre.setDescription(description);

            isUpdated = genreDAO.updateLocalizedGenreInfo(genre, contentLanguage);
        } catch (DAOException e) {
            throw new ServiceException("Error during updating localized genre info", e);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteGenreById(int genreId) throws ServiceException {

        boolean isDeleted;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            GenreDAO genreDAO = daoFactory.getGenreDAO();
            isDeleted = genreDAO.deleteGenre(genreId);
        } catch (DAOException e) {
            throw new ServiceException("Error during deleting genre by id", e);
        }
        return isDeleted;
    }

    @Override
    public boolean checkLocalizedGenreInfoByCode(int genreId, String languageCode)
            throws ServiceException {

        boolean isExist;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            GenreDAO genreDAO = daoFactory.getGenreDAO();
            isExist = genreDAO.checkLocalizedGenreInfoByCode(genreId, languageCode);
        } catch (DAOException e) {
            throw new ServiceException("Error during checking localized info for genre", e);
        }
        return isExist;
    }

    @Override
    public Genre getLocalizedGenreInfoByCode(int genreId, String languageCode)
            throws ServiceException {

        Genre genre;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            GenreDAO genreDAO = daoFactory.getGenreDAO();
            genre = genreDAO.getLocalizedGenreInfoByCode(genreId, languageCode);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting localized genre info by code", e);
        }
        return genre;
    }
}
