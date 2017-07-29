package by.epam.movierating.command.impl.country;

import by.epam.movierating.bean.Country;
import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.command.util.CookieUtil;
import by.epam.movierating.service.CountryService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author serge
 *         27.07.2017.
 */
public class GetCountriesInMovieCommand implements Command {
    private final static Logger logger = Logger.getLogger(GetCountriesInMovieCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int movieId = Integer.parseInt(request.getParameter(ParameterName.MOVIE_ID));
        String currentLanguage = CookieUtil.getCurrentLanguage(request);
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        CountryService countryService = serviceFactory.getCountryService();

        try {
            List<Country> countryList =
                    countryService.getCountriesByMovieId(movieId, currentLanguage);
            String countries = new Gson().toJson(countryList);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(countries);
        } catch (ServiceException e) {
            logger.error("Error during executing GetCountriesInMovieCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
