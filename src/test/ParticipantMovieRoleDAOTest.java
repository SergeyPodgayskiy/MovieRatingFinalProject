import by.epam.movierating.bean.ParticipantMovieRole;
import by.epam.movierating.dao.ParticipantMovieRoleDAO;
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
public class ParticipantMovieRoleDAOTest {
    private static ParticipantMovieRoleDAO participantMovieRoleDAO;
    private static final String LANGUAGE_EN = "en";

    @BeforeClass
    public static void init() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        participantMovieRoleDAO = daoFactory.getParticipantMovieRoleDAO();
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
    public void getMovieRolesByParticipantId() {
        List<ParticipantMovieRole> participantMovieRoles;
        String language = "en_EN";
        int idParticipant = 3; //Frank Darabont
        try {
            participantMovieRoles = participantMovieRoleDAO.getMovieRolesByParticipantId(idParticipant,language);
            Assert.assertNotNull(participantMovieRoles);
            for (ParticipantMovieRole participantMovieRole : participantMovieRoles){
                System.out.println(participantMovieRole.toString());
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
