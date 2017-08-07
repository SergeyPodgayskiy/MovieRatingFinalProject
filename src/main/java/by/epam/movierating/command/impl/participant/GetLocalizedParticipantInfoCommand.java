package by.epam.movierating.command.impl.participant;

import by.epam.movierating.bean.MovieParticipant;
import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.service.MovieParticipantService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         31.07.2017.
 */
public class GetLocalizedParticipantInfoCommand implements Command {
    private static final Logger logger = Logger.getLogger(GetLocalizedParticipantInfoCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";
    private static final String BIRTHDATE_FORMAT = "dd/MM/yyyy";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int participantId = Integer.parseInt(request.getParameter(ParameterName.PARTICIPANT_ID));
        String languageCode = request.getParameter(ParameterName.CONTENT_LANGUAGE);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieParticipantService  participantService = serviceFactory.getMovieParticipantService();
        try {
            MovieParticipant movieParticipant =
                    participantService.getLocalizedParticipantInfo(participantId, languageCode);
            Gson gson = new GsonBuilder().setDateFormat(BIRTHDATE_FORMAT).create();
            String participantJson = gson.toJson(movieParticipant);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().print(participantJson);
        } catch (ServiceException e) {
            logger.error("Error during GetLocalizedParticipantInfoCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
