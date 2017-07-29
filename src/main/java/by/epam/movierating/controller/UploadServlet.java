package by.epam.movierating.controller;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.ParameterName;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author serge
 *         25.07.2017.
 */
public class UploadServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(UploadServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            logger.error("Request is not multipart");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String commandString = request.getParameter(ParameterName.COMMAND);
        Command command = CommandProvider.getInstance().getCommand(commandString);
        command.execute(request, response);
    }
}
