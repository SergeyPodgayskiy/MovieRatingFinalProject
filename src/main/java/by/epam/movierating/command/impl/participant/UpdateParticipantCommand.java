package by.epam.movierating.command.impl.participant;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.service.MovieParticipantService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author serge
 *         31.07.2017.
 */
public class UpdateParticipantCommand implements Command {
    private static final Logger logger = Logger.getLogger(UpdateParticipantCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String birthdateString = request.getParameter(ParameterName.BIRTHDATE);
        Date birthdate = convertStringToDate(birthdateString);
        int participantId = Integer.parseInt(request.getParameter(ParameterName.PARTICIPANT_ID));
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieParticipantService movieParticipantService =
                serviceFactory.getMovieParticipantService();
        try {
            boolean isUpdated = movieParticipantService.updateParticipant(participantId, birthdate);
            String isUpdatedParticipant = new Gson().toJson(isUpdated);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(isUpdatedParticipant);
        } catch (ServiceException e) {
            logger.error("Error during UpdateParticipantCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }

    private Date convertStringToDate(String birthdateInString) { //todo make it in service
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date birthdate = null;
        try {
            birthdate = formatter.parse(birthdateInString);
        } catch (ParseException e) {
            logger.error("Error during converting String to Date", e);
        }
        return birthdate;
    }
}
