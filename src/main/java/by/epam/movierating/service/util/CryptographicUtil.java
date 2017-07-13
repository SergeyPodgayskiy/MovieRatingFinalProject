package by.epam.movierating.service.util;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author serge
 *         17.06.2017.
 */
public class CryptographicUtil {
    public static String encodePassword(byte[] password){
        return DigestUtils.md5Hex(password);
    }
}
