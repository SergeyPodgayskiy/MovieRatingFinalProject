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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         09.05.2017.
 */
public class RegisterCommand implements Command {
    private static final Logger logger = Logger.getLogger(RegisterCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = request.getParameter(ParameterName.LOGIN);
        String email = request.getParameter(ParameterName.EMAIL);
        String fullName = request.getParameter(ParameterName.FULL_NAME);
        byte[] password = request.getParameter(ParameterName.PASSWORD).getBytes();
        byte[] confirmedPassword = request.getParameter(ParameterName.CONFIRMED_PASSWORD).getBytes();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        try {
            User user = userService.register(login, password, confirmedPassword, fullName, email);
            if (user != null) {
                Cookie roleCookie = new Cookie(AttributeName.ROLE, AttributeName.USER);
                Cookie userIdCookie = new Cookie(AttributeName.USER_ID, String.valueOf(user.getId()));
                response.addCookie(userIdCookie);
                response.addCookie(roleCookie);
                response.sendRedirect(PrevPageQueryUtil.getPrevPageQuery(request));
            }
        } catch (ServiceException e) {
            logger.error(e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
