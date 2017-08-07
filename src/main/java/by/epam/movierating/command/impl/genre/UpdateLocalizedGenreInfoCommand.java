package by.epam.movierating.command.impl.genre;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.service.GenreService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         01.08.2017.
 */
public class UpdateLocalizedGenreInfoCommand implements Command {
    private static final Logger logger = Logger.getLogger(UpdateLocalizedGenreInfoCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int genreId = Integer.parseInt(request.getParameter(ParameterName.GENRE_ID));
        String name = request.getParameter(ParameterName.NAME);
        String description = request.getParameter(ParameterName.DESCRIPTION);
        String contentLanguage = request.getParameter(ParameterName.CONTENT_LANGUAGE);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        GenreService genreService = serviceFactory.getGenreService();
        try {
            boolean isUpdated = genreService.updateLocalizedGenreInfo(genreId,
                    name, description, contentLanguage);
        } catch (ServiceException e) {
            logger.error("Error during UpdateLocalizedGenreInfoCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
