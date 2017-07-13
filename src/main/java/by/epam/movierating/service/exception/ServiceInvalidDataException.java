package by.epam.movierating.service.exception;

/**
 * @author serge
 *         20.06.2017.
 */
public class ServiceInvalidDataException extends ServiceException {
    public ServiceInvalidDataException() {
        super();
    }

    public ServiceInvalidDataException(String message) {
        super(message);
    }

    public ServiceInvalidDataException(Exception e) {
        super(e);
    }

    public ServiceInvalidDataException(String message, Exception e) {
        super(message, e);
    }
}
