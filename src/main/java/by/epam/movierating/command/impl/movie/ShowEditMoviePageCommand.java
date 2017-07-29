package by.epam.movierating.command.impl.movie;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         27.07.2017.
 */
public class ShowEditMoviePageCommand implements Command {
    private static final Logger logger = Logger.getLogger(ShowMostDiscussedMoviesCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int movieId = Integer.parseInt(request.getParameter(ParameterName.MOVIE_ID));

        request.setAttribute(AttributeName.MOVIE_ID, movieId);
        request.getRequestDispatcher(PageName.ADD_AND_EDIT_MOVIE_PAGE).forward(request, response);

    }
}
