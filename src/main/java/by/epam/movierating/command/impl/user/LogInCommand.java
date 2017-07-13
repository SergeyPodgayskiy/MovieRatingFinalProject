package by.epam.movierating.command.impl.user;

import by.epam.movierating.bean.User;
import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.command.util.PrevPageQueryUtil;
import by.epam.movierating.service.UserService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
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
public class LogInCommand implements Command {
    private static final Logger logger = Logger.getLogger(LogInCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = request.getParameter(ParameterName.LOGIN);
        byte[] password = request.getParameter(ParameterName.PASSWORD).getBytes();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        try {
            User user = userService.logIn(login, password);
            if (user != null) {
                HttpSession session = request.getSession(); //todo move it
                session.setAttribute(AttributeName.USER_ID, user.getId());
                session.setAttribute(AttributeName.USER, user);
                session.setAttribute(AttributeName.IS_BANNED, user.isBanned());
                if (user.isAdmin()) {
                    session.setAttribute(AttributeName.ROLE, AttributeName.ADMIN); //todo think about it
                    response.sendRedirect(PageName.REDIRECT_TO_ADMIN_PAGE);
                } else {
                    session.setAttribute(AttributeName.ROLE, AttributeName.USER);
//                    response.sendRedirect(request.getParameter(AttributeName.PREVIOUS_PAGE_QUERY));
                    response.sendRedirect(PrevPageQueryUtil.getPrevPageQuery(request));
                }
            }
        } catch (ServiceException e) {
            logger.error(e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
