package by.epam.movierating.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         08.05.2017.
 */
public interface Command {
    void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}
