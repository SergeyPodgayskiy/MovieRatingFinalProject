package by.epam.movierating.command.impl.admin;

import by.epam.movierating.bean.User;
import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.service.UserService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author serge
 *         12.07.2017.
 */
public class ShowUsersCommand implements Command {
    private final static Logger logger = Logger.getLogger(ShowUsersCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        try {
            List<User> userList = userService.getAllUser();
            request.setAttribute(AttributeName.USERS, userList);
            request.getRequestDispatcher(PageName.USERS_PAGE).forward(request,response);
        } catch (ServiceException e) {
            logger.error("Error during executing ShowUsersCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
