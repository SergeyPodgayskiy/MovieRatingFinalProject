package by.epam.movierating.dao.factory;

import by.epam.movierating.dao.*;
import by.epam.movierating.dao.impl.*;

/**
 * @author serge
 *         08.05.2017.
 */
public final class DAOFactory {
    private static final DAOFactory INSTANCE = new DAOFactory();
    private UserDAO userDAO = new UserDAOImpl();
    private CountryDAO countryDAO = new CountryDAOImpl();
    private GenreDAO genreDAO = new GenreDAOImpl();
    private MovieDAO movieDAO = new MovieDAOImpl();
    private MovieParticipantDAO movieParticipantDAO = new MovieParticipantDAOImpl();
    private RatingDAO ratingDAO = new RatingDAOImpl();
    private ReviewDAO reviewDAO = new ReviewDAOImpl();
    private ParticipantMovieRoleDAO participantMovieRoleDAO = new ParticipantMovieRoleDAOImpl();

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return INSTANCE;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public CountryDAO getCountryDAO() {
        return countryDAO;
    }

    public GenreDAO getGenreDAO() {
        return genreDAO;
    }

    public MovieDAO getMovieDAO() {
        return movieDAO;
    }

    public MovieParticipantDAO getMovieParticipantDAO() {
        return movieParticipantDAO;
    }

    public RatingDAO getRatingDAO() {
        return ratingDAO;
    }

    public ReviewDAO getReviewDAO() {
        return reviewDAO;
    }

    public ParticipantMovieRoleDAO getParticipantMovieRoleDAO() {
        return participantMovieRoleDAO;
    }
}
