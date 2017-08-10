package by.epam.movierating.service.impl;

import by.epam.movierating.bean.Movie;
import by.epam.movierating.dao.MovieDAO;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.factory.DAOFactory;
import by.epam.movierating.service.MovieService;
import by.epam.movierating.service.exception.ServiceException;

import java.sql.Time;
import java.util.Date;
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
            movie = movieDAO.getMovieById(idMovie, language);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting Movie by Id", e);
        }
        return movie;
    }

    @Override
    public int addMovie(int releaseYear, String ageLimit, Time duration)
            throws ServiceException {
        int movieId;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();

            Movie movie = new Movie();
            movie.setReleaseYear(releaseYear);
            movie.setAgeLimit(ageLimit);
            movie.setDuration(duration);
            movie.setCreatedAt(new Date());

            movieId = movieDAO.addMovie(movie);
        } catch (DAOException e) {
            throw new ServiceException("Error during adding movie", e);
        }
        return movieId;
    }

    @Override
    public boolean updateMovie(int movieId, int releaseYear, String ageLimit, Time duration)
            throws ServiceException {
        boolean isUpdated;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            Movie movie = new Movie();
            movie.setId(movieId);
            movie.setReleaseYear(releaseYear);
            movie.setAgeLimit(ageLimit);
            movie.setDuration(duration);
            isUpdated = movieDAO.updateMovie(movie);
        } catch (DAOException e) {
            throw new ServiceException("Error during updating movie", e);
        }
        return isUpdated;
    }

    @Override
    public boolean addCountryForMovie(int movieId, String countryCode)
            throws ServiceException {

        boolean isAdded;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            isAdded = movieDAO.addCountryForMovie(movieId, countryCode);
        } catch (DAOException e) {
            throw new ServiceException("Error during adding country for movie", e);
        }
        return isAdded;
    }

    @Override
    public boolean deleteCountryForMovie(int movieId, String countryCode)
            throws ServiceException {

        boolean isDeleted;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            isDeleted = movieDAO.deleteCountryForMovie(movieId, countryCode);
        } catch (DAOException e) {
            throw new ServiceException("Error during deleting country for movie", e);
        }
        return isDeleted;
    }

    @Override
    public boolean addLocalizedMovieInfo(int movieId,
                                         String contentLanguage,
                                         String movieName,
                                         String description,
                                         String slogan) throws ServiceException {

        boolean isAdded;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            Movie movie = new Movie();
            movie.setId(movieId);
            movie.setTitle(movieName);
            movie.setDescription(description);
            movie.setSlogan(slogan);
            isAdded = movieDAO.addLocalizedMovieInfo(movie, contentLanguage);
        } catch (DAOException e) {
            throw new ServiceException("Error during adding language dependent movie info", e);
        }
        return isAdded;
    }

    @Override
    public boolean addParticipantForMovie(int movieId, int participantId, int movieroleId)
            throws ServiceException {

        boolean isAdded;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            isAdded = movieDAO.addParticipantForMovie(movieId, participantId, movieroleId);
        } catch (DAOException e) {
            throw new ServiceException("Error during adding participant for movie", e);
        }
        return isAdded;
    }

    @Override
    public boolean deleteParticipantForMovie(int movieId, int participantId, int movieroleId)
            throws ServiceException {

        boolean isDeleted;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            isDeleted = movieDAO.deleteParticipantForMovie(movieId, participantId, movieroleId);
        } catch (DAOException e) {
            throw new ServiceException("Error during deleting participant for movie", e);
        }
        return isDeleted;
    }

    @Override
    public boolean addGenreForMovie(int movieId, int genreId) throws ServiceException {
        boolean isAdded;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            isAdded = movieDAO.addGenreForMovie(movieId, genreId);
        } catch (DAOException e) {
            throw new ServiceException("Error during adding genre for movie", e);
        }
        return isAdded;
    }

    @Override
    public boolean deleteGenreForMovie(int movieId, int genreId) throws ServiceException {
        boolean isDeleted;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            isDeleted = movieDAO.deleteGenreForMovie(movieId, genreId);
        } catch (DAOException e) {
            throw new ServiceException("Error during deleting genre for movie", e);
        }
        return isDeleted;
    }

    @Override
    public boolean uploadMoviePoster(int movieId, String imgPath)
            throws ServiceException {
        boolean isUploaded;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            isUploaded = movieDAO.uploadMoviePoster(movieId, imgPath);
        } catch (DAOException e) {
            throw new ServiceException("Error during uploading poster for movie", e);
        }
        return isUploaded;
    }

    @Override
    public boolean checkLocalizedMovieInfo(int movieId, String languageCode)
            throws ServiceException {
        boolean isExist;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            isExist = movieDAO.checkLocalizedMovieInfo(movieId, languageCode);
        } catch (DAOException e) {
            throw new ServiceException("Error during checking info for movie", e);
        }
        return isExist;
    }

    @Override
    public Movie getLocalizedMovieInfo(int movieId, String languageCode)
            throws ServiceException {

        Movie movie;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            movie = movieDAO.getLocalizedMovieInfo(movieId, languageCode);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting language movie info by code", e);
        }
        return movie;
    }

    @Override
    public boolean updateLocalizedMovieInfo(int movieId,
                                            String contentLanguage,
                                            String movieName,
                                            String description,
                                            String slogan) throws ServiceException {
        boolean isUpdated;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            Movie movie = new Movie();
            movie.setId(movieId);
            movie.setTitle(movieName);
            movie.setDescription(description);
            movie.setSlogan(slogan);
            isUpdated = movieDAO.updateLocalizedMovieInfo(movie, contentLanguage);
        } catch (DAOException e) {
            throw new ServiceException("Error during updating lang dependent movie info", e);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteMovieById(int movieId) throws ServiceException {
        boolean isDeleted;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            isDeleted = movieDAO.deleteMovie(movieId);
        } catch (DAOException e) {
            throw new ServiceException("Error during deleting movie by id", e);
        }
        return isDeleted;
    }

    @Override
    public String getMoviePoster(int idMovie) throws ServiceException {
        String posterPath;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            posterPath = movieDAO.getMoviePoster(idMovie);
        } catch (DAOException e) {
            throw new ServiceException("Error during get movie poster by movie id", e);
        }
        return posterPath;
    }
}
