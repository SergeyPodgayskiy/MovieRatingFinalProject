package by.epam.movierating.controller.listener;

import by.epam.movierating.dao.connectionpool.ConnectionPool;
import by.epam.movierating.dao.exception.ConnectionPoolException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author serge
 *         28.05.2017.
 */
public class ConnectionPoolListener implements ServletContextListener {

        @Override
        public void contextInitialized(ServletContextEvent servletContextEvent) {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            try {
                connectionPool.initializePool();
            } catch (ConnectionPoolException e) {
                throw new RuntimeException("Can not init a pool", e);
            }
        }

        @Override
        public void contextDestroyed(ServletContextEvent servletContextEvent) {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            try {
                connectionPool.destroyPool();
            } catch (ConnectionPoolException e) {
                throw new RuntimeException("Can not destroy a pool", e);
            }
        }
}
