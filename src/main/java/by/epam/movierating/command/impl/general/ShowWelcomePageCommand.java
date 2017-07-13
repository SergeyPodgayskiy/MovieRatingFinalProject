package by.epam.movierating.command.impl.general;

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
 *         11.06.2017.
 */
public class ShowWelcomePageCommand implements Command {
    private final static Logger logger = Logger.getLogger(ShowWelcomePageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        //todo setSession Attributes and create necessary Services and try..catch block below
     /*   if (session.getAttribute(AttributeName.ROLE).equals(AttributeName.ADMIN)) {
            request.getRequestDispatcher(PageName.ADMIN_PAGE).forward(request, response);
        } else {
            request.getRequestDispatcher(PageName.SHOW_WELCOME_PAGE).forward(request, response);
        }*/
        request.getRequestDispatcher(PageName.WELCOME_PAGE).forward(request, response);
    }
}
