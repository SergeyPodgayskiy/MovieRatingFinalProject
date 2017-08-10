package by.epam.movierating.command.impl.user;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author serge
 *         19.07.2017.
 */
public class CheckRateOpportunityCommand implements Command {
    private static final Logger logger = Logger.getLogger(CheckRateOpportunityCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        response.setContentType("text/plain");
        Boolean isBanned = (Boolean) session.getAttribute(AttributeName.IS_BANNED);

        if (isBanned != null && isBanned) {
            response.getWriter().print(AttributeName.IS_BANNED);
        } else {
            Integer userId = (Integer) session.getAttribute(AttributeName.USER_ID);
            boolean isValidOpportunity = userId != null;
            response.getWriter().print(isValidOpportunity);
        }
    }
}
