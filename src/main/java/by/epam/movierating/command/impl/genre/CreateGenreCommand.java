package by.epam.movierating.command.impl.genre;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.service.GenreService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         01.08.2017.
 */
public class CreateGenreCommand implements Command{
    private static final Logger logger = Logger.getLogger(CreateGenreCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter(ParameterName.NAME);
        String description = request.getParameter(ParameterName.DESCRIPTION);
        String contentLanguage = request.getParameter(ParameterName.CONTENT_LANGUAGE);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        GenreService genreService = serviceFactory.getGenreService();
        try {
            int genreId = genreService.addGenre(name, description,contentLanguage);
            String genreIdJson = new Gson().toJson(genreId);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(genreIdJson);
        } catch (ServiceException e) {
            logger.error("Error during CreateGenreCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
