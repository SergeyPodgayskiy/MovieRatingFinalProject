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
public class AddLocalizedParticipantInfo implements Command {
    private final static Logger logger = Logger.getLogger(AddLocalizedParticipantInfo.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int participantId = Integer.parseInt(request.getParameter(ParameterName.PARTICIPANT_ID));
        String name = request.getParameter(ParameterName.NAME);
        String surname = request.getParameter(ParameterName.SURNAME);
        String contentLanguage = request.getParameter(ParameterName.CONTENT_LANGUAGE);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieParticipantService participantService = serviceFactory.getMovieParticipantService();
        try {
            boolean isAdded =
                    participantService.addLocalizedParticipantInfo(participantId, contentLanguage,
                            name, surname);
        } catch (ServiceException e) {
            logger.error("Error during executing AddLocalizedParticipantInfo", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
