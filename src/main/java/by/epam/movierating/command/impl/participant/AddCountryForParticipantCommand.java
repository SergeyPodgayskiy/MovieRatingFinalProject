package by.epam.movierating.command.impl.participant;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.service.MovieParticipantService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         31.07.2017.
 */
public class AddCountryForParticipantCommand implements Command {
    private final static Logger logger = Logger.getLogger(AddCountryForParticipantCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int participantId = Integer.parseInt(request.getParameter(ParameterName.PARTICIPANT_ID));
        String countryCode = request.getParameter(ParameterName.COUNTRY_CODE);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieParticipantService participantService = serviceFactory.getMovieParticipantService();

        try {
            boolean isAdded = participantService.addCountryForParticipant(participantId, countryCode);
        } catch (ServiceException e) {
            logger.error("Error during executing AddCountryForParticipantCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
