package by.epam.movierating.dao.util;

import by.epam.movierating.dao.connectionpool.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.Connection;

/**
 * @author serge
 *         28.05.2017.
 */
public class DAOUtil {
    private static final Logger logger = Logger.getLogger(DAOUtil.class);

    public static void close(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    if (resource instanceof Connection) {
                        ConnectionPool.getInstance().
                                freeConnection((Connection) resource);
                    } else {
                        resource.close();
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
    }
    public static java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
}
