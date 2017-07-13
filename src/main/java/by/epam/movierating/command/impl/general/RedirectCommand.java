package by.epam.movierating.command.impl.general;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         16.06.2017.
 */
public class RedirectCommand implements Command {
    private final static Logger logger = Logger.getLogger(RedirectCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String redirectPage = request.getParameter(ParameterName.REDIRECT_PAGE);
        String redirectPagePath = defineRedirectPagePath(redirectPage);
        request.getRequestDispatcher(redirectPagePath).forward(request, response); // TODO: 22.06.2017 may be change
    }

    private String defineRedirectPagePath(String redirectPage) {
        switch (redirectPage) {
            case ParameterName.REGISTRATION:
                return PageName.REGISTRATION_PAGE;
           /* case ParameterName.ADD_MOVIE: {
                return PageName.ADD_MOVIE_PAGE;
            }
            case ParameterName.SUCCESS_ADD_MOVIE: {
                return PageName.SUCCESS_MOVIE_ADD;
            }*/
            default: {
                return "";
            }
        }
    }
}
