package by.epam.movierating.dao.util;

import by.epam.movierating.dao.connectionpool.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.Connection;

/**
 * @author serge
 *         12.06.2017.
 */
public interface JDBCAutocloseable {
    Logger logger = Logger.getLogger(DAODefaultFunctional.class);

    default void close(AutoCloseable... resources) {
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
                    logger.error("Can not close resource", e);
                }
            }
        }
    }
}
