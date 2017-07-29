package by.epam.movierating.command.impl.general;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        //todo setSession Attributes and create necessary Services and try..catch block below
//        String role = CookieUtil.getCurrentRole(request);
        String role = (String) request.getSession().getAttribute(AttributeName.ROLE);
        if (role.equals(ParameterName.ADMIN)) {
            request.getRequestDispatcher(PageName.ADMIN_PAGE).forward(request, response);
        } else {
            request.getRequestDispatcher(PageName.WELCOME_PAGE).forward(request, response);
        }
    }
}
