package by.epam.movierating.dao.connectionpool;

import by.epam.movierating.dao.exception.ConnectionPoolException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author serge
 *         09.05.2017.
 */
public final class ConnectionPool {
    private static final ConnectionPool INSTANCE = new ConnectionPool();
    private final static Lock LOCK = new ReentrantLock();
    private BlockingQueue<Connection> availableConnections;
    private BlockingQueue<Connection> usedConnections;
    private volatile boolean isInitializedPool = false;
    private DataBaseResourceManager dataBaseResourceManager;
    private int poolSize;

    private ConnectionPool() {
    }

    public static ConnectionPool getInstance() {
        return INSTANCE;
    }

    public void initializePool() throws ConnectionPoolException {
        LOCK.lock();
        try {
            if (!isInitializedPool) {
                dataBaseResourceManager = DataBaseResourceManager.getInstance();
                initializePoolSize();
                availableConnections = new ArrayBlockingQueue<Connection>(poolSize);
                usedConnections = new ArrayBlockingQueue<Connection>(poolSize);
                addConnectionsInPool();
                isInitializedPool = true;
            } else {
                throw new ConnectionPoolException("Pool is already initialized!");
            }
        } finally {
            LOCK.unlock();
        }
    }

    public Connection getConnection() throws ConnectionPoolException {
        try {
            Connection connection = availableConnections.take();
            usedConnections.add(connection);
            return connection;
        } catch (InterruptedException e) {
            throw new ConnectionPoolException("Can't take available connection", e);
        }
    }

    public void freeConnection(Connection connection) throws ConnectionPoolException {
        if (usedConnections.contains(connection)) {
            usedConnections.remove(connection);
            try {
                availableConnections.put(connection);
            } catch (InterruptedException e) {
                throw new ConnectionPoolException("Can't free a connection", e);
            }
        } else {
            throw new ConnectionPoolException("Try to free not used connection");
        }
    }

    public void destroyPool() throws ConnectionPoolException {
        LOCK.lock();
        try {
            if (isInitializedPool) {
                try {
                    closeAvailableConnections();
                    closeUsedConnections();
                    isInitializedPool = false;
                } catch (SQLException e) {
                    throw new ConnectionPoolException("Can't close connections", e);
                }
            } else {
                throw new ConnectionPoolException("Try to destroy not initialized pool");
            }
        } finally {
            LOCK.unlock();
        }
    }

    private void addConnectionsInPool() throws ConnectionPoolException {
        try {
            loadDriverClass(); //todo may be useless
            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(
                        dataBaseResourceManager.getValue(DataBaseParameter.DB_URL),
                        dataBaseResourceManager.getValue(DataBaseParameter.DB_USER),
                        dataBaseResourceManager.getValue(DataBaseParameter.DB_PASSWORD)
                );
                availableConnections.add(connection);
            }
        } catch (SQLException e) {
            throw new ConnectionPoolException("Can't get a connection", e);
        }
    }

    private void loadDriverClass() throws ConnectionPoolException {
        try {
            Class.forName(dataBaseResourceManager.getValue(DataBaseParameter.DB_DRIVER));
        } catch (ClassNotFoundException e) {
            throw new ConnectionPoolException("Database driver is not found", e);
        }
    }

    private void closeAvailableConnections() throws ConnectionPoolException, SQLException {
        for (Connection connection : availableConnections) {
            connection.close();
        }
    }

    private void closeUsedConnections() throws ConnectionPoolException, SQLException {
        for (Connection connection : usedConnections) {
            connection.close();
        }
    }

    private void initializePoolSize() {
        int defaultPoolSize = 5;
        try {
            poolSize = Integer.parseInt(dataBaseResourceManager.
                    getValue(DataBaseParameter.DB_POOL_SIZE));
        } catch (NumberFormatException e) {
            poolSize = defaultPoolSize;
        }
    }
}