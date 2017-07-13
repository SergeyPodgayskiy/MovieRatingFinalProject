package by.epam.movierating.command.impl.user;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.command.util.PrevPageQueryUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         06.07.2017.
 */
public class ChangeLanguageCommand implements Command {
    private final static Logger logger = Logger.getLogger(ChangeLanguageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userLanguage = request.getParameter(ParameterName.USER_LANGUAGE).trim();

        Cookie currentLanguage = new Cookie(AttributeName.USER_LANGUAGE, userLanguage);

        response.addCookie(currentLanguage);
        response.sendRedirect(PrevPageQueryUtil.getPrevPageQuery(request));
    }
}
