package by.epam.movierating.command.impl.participant;

import by.epam.movierating.bean.MovieParticipant;
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
 *         21.07.2017.
 */
public class GetAllParticipantsByRoleIdCommand implements Command {
    private final static Logger logger = Logger.getLogger(GetAllParticipantsByRoleIdCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String currentLanguage = CookieUtil.getCurrentLanguage(request);
        int roleId = Integer.parseInt(request.getParameter(ParameterName.ROLE_ID));
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieParticipantService movieParticipantService =
                serviceFactory.getMovieParticipantService();

        try {
            List<MovieParticipant> participantList =
                    movieParticipantService.getAllParticipantsByRoleId(roleId, currentLanguage);
            String json = new Gson().toJson(participantList);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(json);
        } catch (ServiceException e) {
            logger.error("Error during executing GetAllParticipantsByRoleIdCommand command", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
