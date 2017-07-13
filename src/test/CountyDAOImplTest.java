import by.epam.movierating.bean.Country;
import by.epam.movierating.dao.CountryDAO;
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
public class CountyDAOImplTest {
    private static CountryDAO countryDAO;
    private static final String LANGUAGE_EN = "en";

    @BeforeClass
    public static void init() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        countryDAO = daoFactory.getCountryDAO();
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
    public void addCountry() {
        String countryCode = "eng";
        String countryName = "England";
        Country country = new Country();
        country.setCode(countryCode);
        country.setName(countryName);
        boolean isAdded;
        try {
            isAdded = countryDAO.addCountry(country);
            Assert.assertTrue(isAdded);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCountriesByMovieId() {
        String language = "en_EN";
        List<Country> countryList;
        int idMovie = 1;
        try {
            countryList = countryDAO.getCountriesByMovieId(idMovie, language);
            Assert.assertNotNull(countryList);
            for (Country country : countryList){
                System.out.println(country.toString());
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
