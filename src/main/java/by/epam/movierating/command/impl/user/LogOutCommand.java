package by.epam.movierating.command.impl.user;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.command.util.CookieUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author serge
 *         09.05.2017.
 */
public class LogOutCommand implements Command {
    private static final Logger logger = Logger.getLogger(LogOutCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            String role = (String) session.getAttribute(AttributeName.ROLE);
            if (!(role.equals(ParameterName.ADMIN))) {
                CookieUtil.deleteCookie(request, response,
                        AttributeName.USER_ID, AttributeName.ROLE);
            }
            session.invalidate();
            response.sendRedirect(PageName.REDIRECT_TO_WELCOME_PAGE);
        }
    }
}
