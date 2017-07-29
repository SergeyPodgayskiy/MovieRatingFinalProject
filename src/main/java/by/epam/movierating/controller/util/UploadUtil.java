package by.epam.movierating.controller.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author serge
 *         25.07.2017.
 */
public class UploadUtil {
    private static final String TEMP_DIR = "javax.servlet.context.tempdir";
    private static final String PARTICIPANT = "participant";
    private static final String MOVIE = "movie";
    private static final String TARGET_PATH = "target\\movie_rating\\WEB-INF";
    private static final String PARTICIPANT_PATH = "src\\main\\webapp\\images\\participant";
    private static final String MOVIE_PATH = "src\\main\\webapp\\images\\poster";
    private static final String TARGET_ACTOR_PATH = "\\images\\actor\\";
    private static final String TARGET_MOVIE_PATH = "\\images\\poster\\";
    private static final String IMAGES_PARTICIPANT = "images\\participant\\";
    private static final String IMAGES_POSTER = "images\\poster\\";
    private static final String WEB_INF = "\\WEB-INF\\";
    private static final DiskFileItemFactory factory;
    private static final int SIZE=1024;
    private static final int TEN=10;
    static {
        factory = new DiskFileItemFactory();
        factory.setSizeThreshold(SIZE * SIZE);
    }

    /**
     * Instantiates and initializes ServletFileUpload object
     * @param request {@link HttpServletRequest} object
     * @return {@link ServletFileUpload} object
     */
    public static ServletFileUpload createServletFileUpload(HttpServletRequest request) {

        ServletContext servletContext = request.getSession().getServletContext();
        File tempDir = (File) servletContext.getAttribute(TEMP_DIR);
        factory.setRepository(tempDir);

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(SIZE * SIZE * TEN);
        return upload;
    }

    /**
     * Writes the file and creates file's name to store this in data storage
     * @param item Represents a file or form item that was received within a
     * multipart/form-data POST request.
     * @param servletContext {@link ServletContext} object
     * @param entityName name of the entity wants to save a file in
     * @return path to this file
     * @throws Exception
     */
    public static String processUploadedFile(FileItem item, ServletContext servletContext,
                                             String entityName)
            throws Exception {
        File uploadedFile;

        String path = servletContext.getRealPath(WEB_INF + item.getName());
        String programPath = buildProgramImagePath(entityName, path);
        uploadedFile = new File(programPath);
        //noinspection ResultOfMethodCallIgnored
        uploadedFile.createNewFile();
        item.write(uploadedFile);

        String targetPath = buildTargetPath(entityName, path);
        uploadedFile = new File(targetPath);
        //noinspection ResultOfMethodCallIgnored
        uploadedFile.createNewFile();
        item.write(uploadedFile);

        return buildPathForEntity(entityName, item.getName());
    }

    /**
     * Constructs path to this file in project's scope
     * @param entityName name of the entity who owns this file
     * @param oldPath recent path to the file
     * @return new path to the file in project's scope
     */
    private static String buildProgramImagePath(String entityName, String oldPath) {
        switch (entityName) {
            case PARTICIPANT:
                return oldPath.replace(TARGET_PATH, PARTICIPANT_PATH);
            case MOVIE:
                return oldPath.replace(TARGET_PATH, MOVIE_PATH);
            default:
                return "";
        }
    }

    /**
     * Constructs path to this file in target's scope
     * @param entityName name of the entity who owns this file
     * @param oldPath recent path to the file
     * @return new path to the file in target's scope
     */
    private static String buildTargetPath(String entityName, String oldPath) {
        switch (entityName) {
            case PARTICIPANT:
                return oldPath.replace(WEB_INF, TARGET_ACTOR_PATH);
            case MOVIE:
                return oldPath.replace(WEB_INF, TARGET_MOVIE_PATH);
            default:
                return "";
        }
    }

    /**
     * Constructs path to this file in data storage
     * @param entityName name of the entity who owns this file
     * @param currentName name of the file to store
     * @return path to this file
     */
    private static String buildPathForEntity(String entityName, String currentName) {
        switch (entityName) {
            case PARTICIPANT:
                return IMAGES_PARTICIPANT + currentName;
            case MOVIE:
                return IMAGES_POSTER + currentName;
            default:
                return "";
        }
    }
}
