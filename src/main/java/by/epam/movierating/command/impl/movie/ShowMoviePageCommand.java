package by.epam.movierating.command.impl.movie;

import by.epam.movierating.bean.*;
import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.command.util.CookieUtil;
import by.epam.movierating.service.*;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author serge
 *         30.06.2017.
 */
public class ShowMoviePageCommand implements Command {
    private static final Logger logger = Logger.getLogger(ShowMoviePageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String currentLanguage = CookieUtil.getCurrentLanguage(request);
        HttpSession session = request.getSession(); //todo replace it
        int movieId = Integer.parseInt(request.getParameter(ParameterName.MOVIE_ID));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieService movieService = serviceFactory.getMovieService();
        CountryService countryService = serviceFactory.getCountryService();
        GenreService genreService = serviceFactory.getGenreService();
        MovieParticipantService movieParticipantService = serviceFactory.getMovieParticipantService();
        RatingService ratingService = serviceFactory.getRatingService();
        ReviewService reviewService = serviceFactory.getReviewService();

        try {
            Movie movie = movieService.getMovieById(movieId,currentLanguage);
            request.setAttribute(AttributeName.MOVIE, movie);
            List<Country> countryList = countryService.getCountriesByMovieId(movieId,currentLanguage);
            request.setAttribute(AttributeName.COUNTRIES, countryList);
            List<Genre> genreList = genreService.getGenresByMovieId(movieId,currentLanguage);
            request.setAttribute(AttributeName.GENRES, genreList);
            Map<MovieParticipant, List<MovieRole>> movieParticipantMap
                    = movieParticipantService.getMovieParticipantsByMovieId(movieId,currentLanguage);
            request.setAttribute(AttributeName.PARTICIPANTS, movieParticipantMap);
            if (session.getAttribute(AttributeName.USER_ID) != null) {
                int userId = (int) session.getAttribute(AttributeName.USER_ID);
                Rating rating = ratingService.getUserMovieRatingByUserId(movieId, userId);
                if (rating != null) {
                    request.setAttribute(AttributeName.USER_MOVIE_RATING, rating);
                }
            }
            request.getRequestDispatcher(PageName.MOVIE_INFO_PAGE).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Error during executing ShowMoviePageCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
