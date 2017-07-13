package by.epam.movierating.command.impl.movie;

import by.epam.movierating.bean.Movie;
import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.util.DefineLanguageUtil;
import by.epam.movierating.service.MovieService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author serge
 *         23.06.2017.
 */
public class ShowTopMoviesCommand implements Command {
    private static final Logger logger = Logger.getLogger(ShowTopMoviesCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String currentLanguage = DefineLanguageUtil.getCurrentLanguage(request);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieService movieService = serviceFactory.getMovieService();
        try {
            List<Movie> movieList = movieService.getTopMovies(currentLanguage);
            request.setAttribute(AttributeName.MOVIES, movieList);
            request.getRequestDispatcher(PageName.TOP_MOVIES_PAGE).forward(request, response);
        } catch (ServiceException e) {
            logger.error(e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
