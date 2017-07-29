import by.epam.movierating.bean.MovieRole;
import by.epam.movierating.dao.MovieRoleDAO;
import by.epam.movierating.dao.connectionpool.ConnectionPool;
import by.epam.movierating.dao.exception.ConnectionPoolException;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.factory.DAOFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * @author serge
 *         11.06.2017.
 */
public class MovieRoleDAOTest {
    private static MovieRoleDAO movieRoleDAO;
    private static final String LANGUAGE_EN = "en";

    @BeforeClass
    public static void init() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        movieRoleDAO = daoFactory.getMovieRoleDAO();
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try {
            connectionPool.initializePool();
        } catch (ConnectionPoolException e) {
            throw new RuntimeException("Can not init a pool", e);
        }
    }

    @AfterClass
    public static void destroy() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try {
            connectionPool.destroyPool();
        } catch (ConnectionPoolException e) {
            throw new RuntimeException("Can not dispose a pool", e);
        }
    }

    @Test
    public void getRolesInMovieByParticipantId() {
        List<MovieRole> movieRoles;
        String language = "en_EN";
        int idParticipant = 3; //Frank Darabont
        int idMovie = 3; //Frank Darabont
        try {
            movieRoles = movieRoleDAO.getRolesInMovieByParticipantId(idParticipant, idMovie, language);
            Assert.assertNotNull(movieRoles);
            for (MovieRole movieRole : movieRoles) {
                System.out.println(movieRole.toString());
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
