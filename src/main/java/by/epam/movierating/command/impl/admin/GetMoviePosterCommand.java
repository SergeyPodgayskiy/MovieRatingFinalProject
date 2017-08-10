package by.epam.movierating.command.impl.admin;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.service.MovieService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         10.08.2017.
 */
public class GetMoviePosterCommand implements Command {
    private final static Logger logger = Logger.getLogger(GetMoviePosterCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idMovie = Integer.parseInt(request.getParameter(ParameterName.MOVIE_ID));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieService movieService = serviceFactory.getMovieService();

        try {
            String posterPath =
                    movieService.getMoviePoster(idMovie);
            String json = new Gson().toJson(posterPath);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(json);
        } catch (ServiceException e) {
            logger.error("Error during executing GetMoviePosterCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
