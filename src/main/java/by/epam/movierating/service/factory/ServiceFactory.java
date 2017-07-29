package by.epam.movierating.service.factory;

import by.epam.movierating.service.*;
import by.epam.movierating.service.impl.*;

/**
 * @author serge
 *         09.05.2017.
 */
public final class ServiceFactory {
    private static final ServiceFactory INSTANCE = new ServiceFactory();

    private UserService userService = new UserServiceImpl();
    private MovieService movieService = new MovieServiceImpl();
    private CountryService countryService = new CountryServiceImpl();
    private GenreService genreService = new GenreServiceImpl();
    private ReviewService reviewService = new ReviewServiceImpl();
    private MovieParticipantService movieParticipantService
            = new MovieParticipantServiceImpl();
    private RatingService ratingService = new RatingServiceImpl();
    private MovieRoleService movieRoleService =
            new MovieRoleServiceImpl();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    public UserService getUserService() {
        return userService;
    }

    public MovieService getMovieService() {
        return movieService;
    }

    public CountryService getCountryService() {
        return countryService;
    }

    public GenreService getGenreService() {
        return genreService;
    }

    public ReviewService getReviewService() {
        return reviewService;
    }

    public MovieParticipantService getMovieParticipantService() {
        return movieParticipantService;
    }

    public RatingService getRatingService() {
        return ratingService;
    }

    public MovieRoleService getMovieRoleService() {
        return movieRoleService;
    }
}
