package by.epam.movierating.command.impl.movie;

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
import java.sql.Time;

/**
 * @author serge
 *         22.07.2017.
 */
public class CreateMovieCommand implements Command {
    private static final Logger logger = Logger.getLogger(CreateMovieCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int releaseYear = Integer.parseInt(request.getParameter(ParameterName.RELEASE_YEAR));
        String ageLimit = request.getParameter(ParameterName.AGE_LIMIT);
        Time duration = Time.valueOf(request.getParameter(ParameterName.DURATION));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieService movieService = serviceFactory.getMovieService();
        try {
            int movieId = movieService.addMovie(releaseYear, ageLimit, duration);
            String movieIdJson = new Gson().toJson(movieId);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(movieIdJson);
        } catch (ServiceException e) {
            logger.error("Error during CreateMovieCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
