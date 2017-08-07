package by.epam.movierating.command.impl.participant;

import by.epam.movierating.bean.MovieRole;
import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.command.util.CookieUtil;
import by.epam.movierating.service.MovieParticipantService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author serge
 *         06.08.2017.
 */
public class GetParticipantRolesCommand implements Command {
    private final static Logger logger = Logger.getLogger(GetParticipantRolesCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String currentLanguage = CookieUtil.getCurrentLanguage(request);
        int participantId = Integer.parseInt(request.getParameter(ParameterName.PARTICIPANT_ID));
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieParticipantService participantService = serviceFactory.getMovieParticipantService();

        try {
            List<MovieRole> movieRoles =
                    participantService.getParticipantRoles(participantId, currentLanguage);
            String json = new Gson().toJson(movieRoles);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(json);
        } catch (ServiceException e) {
            logger.error("Error during executing GetParticipantRolesCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
