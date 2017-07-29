import by.epam.movierating.bean.Genre;
import by.epam.movierating.dao.GenreDAO;
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
public class GenreDAOImplTest {
    private static GenreDAO genreDAO;
    private static final String LANGUAGE_EN = "en";

    @BeforeClass
    public static void init() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        genreDAO = daoFactory.getGenreDAO();
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
    public void getGenresByMovieId() {
        String language = "en_EN";
        List<Genre> genreList;
        int idMovie = 1;
        try {
            genreList = genreDAO.getGenresByMovieId(idMovie, language);
            Assert.assertNotNull(genreList);
            for (Genre genre : genreList) {
                System.out.println(genre.toString());
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllGenres() {
        String language = "en_EN";
        List<Genre> genreList;
        int idMovie = 1;
        try {
            genreList = genreDAO.getAllGenres(language);
            Assert.assertNotNull(genreList);
            for (Genre genre : genreList) {
                System.out.println(genre.toString());
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
