package by.epam.movierating.command.impl.user;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.service.ReviewService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author serge
 *         09.08.2017.
 */
public class DeleteReviewCommand implements Command {
    private final static Logger logger = Logger.getLogger(DeleteReviewCommand.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        int idMovie = Integer.parseInt(request.getParameter(ParameterName.MOVIE_ID));
        int idUser = (Integer) session.getAttribute(AttributeName.USER_ID);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        ReviewService reviewService = serviceFactory.getReviewService();

        try {
            boolean isDeleted =
                    reviewService.deleteReview(idMovie,idUser);
            String json = new Gson().toJson(isDeleted);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(json);
        } catch (ServiceException e) {
            logger.error("Error during executing DeleteReviewCommand", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }
}
