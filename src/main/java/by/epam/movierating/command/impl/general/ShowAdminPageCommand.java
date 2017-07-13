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
 *         12.07.2017.
 */
public class ShowAdminPageCommand implements Command {
    private final static Logger logger = Logger.getLogger(ShowAdminPageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        request.getRequestDispatcher(PageName.ADMIN_PAGE).forward(request, response);
    }
}
