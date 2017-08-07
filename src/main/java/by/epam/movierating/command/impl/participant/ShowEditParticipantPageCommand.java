package by.epam.movierating.command.impl.participant;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         05.08.2017.
 */
public class ShowEditParticipantPageCommand implements Command{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int participantId = Integer.parseInt(request.getParameter(ParameterName.PARTICIPANT_ID));

        request.setAttribute(AttributeName.PARTICIPANT_ID, participantId);
        request.getRequestDispatcher(PageName.ADD_AND_EDIT_PARTICIPANT_PAGE).forward(request, response);
    }
}
