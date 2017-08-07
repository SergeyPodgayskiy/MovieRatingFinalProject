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
public class CreateParticipantCommand implements Command {
    private static final Logger logger = Logger.getLogger(CreateParticipantCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String birthdateString = request.getParameter(ParameterName.BIRTHDATE);
        Date birthdate = convertStringToDate(birthdateString);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieParticipantService movieParticipantService =
                serviceFactory.getMovieParticipantService();
        try {
            int participantId = movieParticipantService.addParticipant(birthdate);
            String participantIdJson = new Gson().toJson(participantId);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(participantIdJson);
        } catch (ServiceException e) {
            logger.error("Error during CreateParticipantCommand", e);
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
