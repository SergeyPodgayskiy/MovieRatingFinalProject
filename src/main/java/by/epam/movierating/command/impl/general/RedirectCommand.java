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

        if (redirectPage != null) {
            String redirectPagePath = defineRedirectPagePath(redirectPage);
            request.getRequestDispatcher(redirectPagePath).forward(request, response);
        } else {
            response.sendRedirect(PageName.REDIRECT_TO_WELCOME_PAGE);
        }
    }

    private String defineRedirectPagePath(String redirectPage) {
        switch (redirectPage) {
            case ParameterName.REGISTRATION:
                return PageName.REGISTRATION_PAGE;
            case ParameterName.ADD_MOVIE_PAGE: {
                return PageName.ADD_AND_EDIT_MOVIE_PAGE;
            }
            case ParameterName.ADD_PARTICIPANT_PAGE: {
                return PageName.ADD_AND_EDIT_PARTICIPANT_PAGE;
            }
            case ParameterName.ADD_GENRE_PAGE: {
                return PageName.ADD_AND_EDIT_GENRE_PAGE;
            }
           /*
            case ParameterName.SUCCESS_ADD_MOVIE: {
                return PageName.SUCCESS_MOVIE_ADD;
            }*/
            default: {
                return "";
            }
        }
    }
}
