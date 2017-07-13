package by.epam.movierating.command.impl.user;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.PageName;
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
            session.invalidate();
            response.sendRedirect(PageName.REDIRECT_TO_WELCOME_PAGE); // TODO: 21.06.2017 may be change
        }
    }
}
