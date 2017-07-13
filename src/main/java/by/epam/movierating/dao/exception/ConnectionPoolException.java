package by.epam.movierating.dao.exception;

/**
 * @author serge
 *         09.05.2017.
 */
public class ConnectionPoolException extends Exception{

    public ConnectionPoolException() {
        super();
    }

    public ConnectionPoolException(String message) {
        super(message);
    }

    public ConnectionPoolException(Exception e){
        super(e);
    }

    public ConnectionPoolException(String message, Exception e) {
        super(message, e);
    }
}
