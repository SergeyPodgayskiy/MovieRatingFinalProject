package by.epam.movierating.command.impl.upload;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.controller.util.UploadUtil;
import by.epam.movierating.service.MovieParticipantService;
import by.epam.movierating.service.factory.ServiceFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author serge
 *         10.08.2017.
 */
public class UploadParticipantPhotoCommand implements Command {
    private final static Logger logger = Logger.getLogger(UploadParticipantPhotoCommand.class);
    private static final String PARTICIPANT = "participant";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletFileUpload upload = UploadUtil.createServletFileUpload(request);
        int idParticipant = Integer.parseInt(request.getParameter(ParameterName.PARTICIPANT_ID));
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieParticipantService participantService = serviceFactory.getMovieParticipantService();

        ServletContext servletContext = request.getSession().getServletContext();

        try {
            List<FileItem> fileItems = upload.parseRequest(request);
            for (FileItem item : fileItems) {
                if (!item.isFormField()) {
                    String fileName = UploadUtil.processUploadedFile(item, servletContext, PARTICIPANT);
                    boolean result = participantService.uploadParticipantPhoto(idParticipant, fileName);
                    response.getWriter().print(true);
                }
            }
        } catch (FileUploadException e) {
            logger.error("Error during uploading file", e);
            response.getWriter().print(false);
        } catch (Exception e) {
            logger.error(e);
            response.getWriter().print(false);
        }
    }
}
