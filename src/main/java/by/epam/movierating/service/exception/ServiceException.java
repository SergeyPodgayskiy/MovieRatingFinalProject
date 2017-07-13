package by.epam.movierating.service.exception;

/**
 * @author serge
 *         28.05.2017.
 */
public class ServiceException extends Exception {

    public ServiceException(){
        super();
    }
    public ServiceException(String message){
        super(message);
    }
    public ServiceException(Exception e){
        super(e);
    }
    public ServiceException(String message, Exception e){
        super(message,e);
    }
}
