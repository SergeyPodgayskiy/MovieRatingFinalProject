package by.epam.movierating.command.impl.user;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.service.RatingService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author serge
 *         06.07.2017.
 */
public class RateMovieCommand implements Command {
    private static final Logger logger = Logger.getLogger(RateMovieCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        int idMovie = Integer.parseInt(request.getParameter(ParameterName.MOVIE_ID));
        int idUser = (Integer) session.getAttribute(AttributeName.USER_ID);
        BigDecimal mark = new BigDecimal(Double.parseDouble(request.getParameter(ParameterName.MARK))); //todo
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        RatingService ratingService = serviceFactory.getRatingService();
        try {
            boolean isRated = ratingService.rateMovie(idMovie,idUser,mark);
        } catch (ServiceException e) {
            logger.error(e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
