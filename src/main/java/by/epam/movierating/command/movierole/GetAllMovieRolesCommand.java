package by.epam.movierating.command.movierole;

import by.epam.movierating.bean.MovieRole;
import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.util.CookieUtil;
import by.epam.movierating.service.MovieRoleService;
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
 *         20.07.2017.
 */
public class GetAllMovieRolesCommand implements Command {
    private final static Logger logger = Logger.getLogger(GetAllMovieRolesCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String currentLanguage = CookieUtil.getCurrentLanguage(request);
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieRoleService movieRoleService =
                serviceFactory.getMovieRoleService();

        try {
            List<MovieRole> movieRoles =
                    movieRoleService.getAllMovieRoles(currentLanguage);
            String json = new Gson().toJson(movieRoles);

            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(json);
        } catch (ServiceException e) {
            logger.error("Error during executing GetAllMovieRolesCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
