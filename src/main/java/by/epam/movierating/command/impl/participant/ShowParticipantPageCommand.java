package by.epam.movierating.command.impl.participant;

import by.epam.movierating.bean.Country;
import by.epam.movierating.bean.MovieParticipant;
import by.epam.movierating.bean.MovieRole;
import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.command.util.CookieUtil;
import by.epam.movierating.service.CountryService;
import by.epam.movierating.service.MovieParticipantService;
import by.epam.movierating.service.MovieRoleService;
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
 *         05.08.2017.
 */
public class ShowParticipantPageCommand implements Command {
    private static final Logger logger = Logger.getLogger(ShowParticipantPageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String currentLanguage = CookieUtil.getCurrentLanguage(request);
        int participantId = Integer.parseInt(request.getParameter(ParameterName.PARTICIPANT_ID));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieParticipantService participantService = serviceFactory.getMovieParticipantService();
        CountryService countryService = serviceFactory.getCountryService();
        MovieRoleService movieRoleService = serviceFactory.getMovieRoleService();

        try {
            MovieParticipant participant = participantService.getParticipantById(participantId, currentLanguage);
            request.setAttribute(AttributeName.PARTICIPANT, participant);
            String countryCode = participant.getCountry().getCode();
            Country country = countryService.getCountryByCode(countryCode, currentLanguage);
            request.setAttribute(AttributeName.COUNTRY, country);
            List<MovieRole> movieRoles = movieRoleService.getMovieRolesByParticipantId(participantId, currentLanguage);
            request.setAttribute(AttributeName.PARTICIPANT_MOVIE_ROLES, movieRoles);
            request.getRequestDispatcher(PageName.PARTICIPANT_INFO_PAGE).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Error during executing ShowParticipantPageCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
