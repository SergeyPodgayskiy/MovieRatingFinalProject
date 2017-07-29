package by.epam.movierating.command.impl.admin;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.service.UserService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         14.07.2017.
 */
public class UpdateBanStatusCommand implements Command {
    private final static Logger logger = Logger.getLogger(UpdateBanStatusCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idUser = Integer.parseInt(request.getParameter(ParameterName.USER_ID));
        boolean currentBanStatus = Boolean.parseBoolean(request.getParameter(ParameterName.BAN_STATUS));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();

        try {
            boolean isUpdated = userService.updateBanStatus(idUser, currentBanStatus);
            if (isUpdated) {
                request.setAttribute(AttributeName.BAN_STATUS, !currentBanStatus);
            } else {
                request.setAttribute(AttributeName.BAN_STATUS, currentBanStatus);
            }

        } catch (ServiceException e) {
            logger.error("Error during updating ban status", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
