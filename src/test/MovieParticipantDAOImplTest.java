import by.epam.movierating.bean.MovieParticipant;
import by.epam.movierating.dao.MovieParticipantDAO;
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
public class MovieParticipantDAOImplTest {
    private static MovieParticipantDAO movieParticipantDAO;
    private static final String LANGUAGE_EN = "en";

    @BeforeClass
    public static void init() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        movieParticipantDAO = daoFactory.getMovieParticipantDAO();
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
    public void getMovieParticipantByIdTest()  {
        String language = "en_EN";
        MovieParticipant movieParticipant = null;
        try {
            movieParticipant = movieParticipantDAO.getMovieParticipantById(1,language);
            Assert.assertNotNull(movieParticipant);
            System.out.println(movieParticipant);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMovieParticipantsByMovieId() {
        List<MovieParticipant> movieParticipantList;
        String language = "en_EN";
        int idMovie = 1;
        try {
            movieParticipantList = movieParticipantDAO.getMovieParticipantsByMovieId(idMovie,language);
            Assert.assertNotNull(movieParticipantList);
            for(MovieParticipant movieParticipant : movieParticipantList) {
                System.out.println(movieParticipant.toString());
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
