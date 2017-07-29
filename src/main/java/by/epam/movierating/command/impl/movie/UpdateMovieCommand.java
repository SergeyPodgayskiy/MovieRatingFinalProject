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
 *         26.07.2017.
 */
public class UpdateMovieCommand implements Command {
    private static final Logger logger = Logger.getLogger(UpdateMovieCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int movieId = Integer.parseInt(request.getParameter(ParameterName.MOVIE_ID));
        int releaseYear = Integer.parseInt(request.getParameter(ParameterName.RELEASE_YEAR));
        String ageLimit = request.getParameter(ParameterName.AGE_LIMIT);
        Time duration = Time.valueOf(request.getParameter(ParameterName.DURATION));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieService movieService = serviceFactory.getMovieService();
        try {
            boolean isUpdated = movieService.updateMovie(movieId, releaseYear, ageLimit, duration);
            String isUpdatedMovie = new Gson().toJson(isUpdated);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(isUpdatedMovie);
        } catch (ServiceException e) {
            logger.error("Error during UpdateMovieCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
