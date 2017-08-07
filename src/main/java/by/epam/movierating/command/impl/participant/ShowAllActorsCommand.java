package by.epam.movierating.command.impl.participant;

import by.epam.movierating.bean.Movie;
import by.epam.movierating.bean.MovieParticipant;
import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.command.util.CookieUtil;
import by.epam.movierating.service.MovieParticipantService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author serge
 *         06.08.2017.
 */
public class ShowAllActorsCommand implements Command {
    private static final Logger logger = Logger.getLogger(ShowAllActorsCommand.class);
    private static final int DEFAULT_PAGE_NUMBER = 1;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String currentLanguage = CookieUtil.getCurrentLanguage(request);
        String currentPage = request.getParameter(ParameterName.CURRENT_PAGE_NUMBER);
        int currentPageNumber = currentPage == null ? DEFAULT_PAGE_NUMBER : Integer.parseInt(currentPage);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieParticipantService participantService = serviceFactory.getMovieParticipantService();

        try {
            Map<MovieParticipant,List<Movie>> actorMap =
                    participantService.getAllLimitedActors(currentLanguage, currentPageNumber);

            request.setAttribute(AttributeName.PARTICIPANTS, actorMap);
            request.setAttribute(AttributeName.CURRENT_PAGE_NUMBER, currentPageNumber);

            request.getRequestDispatcher(PageName.ACTORS_PAGE).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Error during executing ShowAllActorsCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
