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
public class AddParticipantForMovieCommand implements Command {
    private final static Logger logger = Logger.getLogger(AddParticipantForMovieCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int movieId = Integer.parseInt(request.getParameter(ParameterName.MOVIE_ID));
        int participantId = Integer.parseInt(request.getParameter(ParameterName.PARTICIPANT_ID));
        int movieroleId = Integer.parseInt(request.getParameter(ParameterName.ROLE_ID));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieService movieService = serviceFactory.getMovieService();
        try {
            boolean isAdded =
                    movieService.addParticipantForMovie(movieId, participantId, movieroleId);
        } catch (ServiceException e) {
            logger.error("Error during executing AddParticipantForMovieCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
