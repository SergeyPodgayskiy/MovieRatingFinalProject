package by.epam.movierating.command.impl.movie;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.service.MovieService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         24.07.2017.
 */
public class AddLanguageDependentMovieInfoCommand implements Command {
    private final static Logger logger = Logger.getLogger(AddLanguageDependentMovieInfoCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int movieId = Integer.parseInt(request.getParameter(ParameterName.MOVIE_ID));
        String movieName = request.getParameter(ParameterName.MOVIE_NAME);
        String description = request.getParameter(ParameterName.DESCRIPTION);
        String slogan = request.getParameter(ParameterName.SLOGAN);
        String contentLanguage = request.getParameter(ParameterName.CONTENT_LANGUAGE);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieService movieService = serviceFactory.getMovieService();
        try {
            boolean isAdded =
                    movieService.addLanguageDependentMovieInfo(movieId, contentLanguage,
                            movieName, description, slogan);
        } catch (ServiceException e) {
            logger.error("Error during executing AddLanguageDependentMovieInfoCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
