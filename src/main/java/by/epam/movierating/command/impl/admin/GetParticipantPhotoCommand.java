package by.epam.movierating.command.impl.admin;

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

/**
 * @author serge
 *         10.08.2017.
 */
public class GetParticipantPhotoCommand implements Command {
    private final static Logger logger = Logger.getLogger(GetParticipantPhotoCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idParticipant = Integer.parseInt(request.getParameter(ParameterName.PARTICIPANT_ID));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieParticipantService participantService = serviceFactory.getMovieParticipantService();

        try {
            String photoPath =
                    participantService.getParticipantPhoto(idParticipant);
            String json = new Gson().toJson(photoPath);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(json);
        } catch (ServiceException e) {
            logger.error("Error during executing GetParticipantPhotoCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
