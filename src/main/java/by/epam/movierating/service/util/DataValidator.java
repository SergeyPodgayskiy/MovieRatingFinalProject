package by.epam.movierating.service.util;

import by.epam.movierating.service.exception.ServiceInvalidDataException;

import java.util.Arrays;

/**
 * @author serge
 *         20.06.2017.
 */
public class DataValidator {
    private static final int MIN_PASSWORD_LENGTH = 0; //todo correct values
    private static final int MAX_PASSWORD_LENGTH = 100;

    public static boolean validatePassword(byte[] password)
            throws ServiceInvalidDataException {
        if (password != null
                && checkPasswordLength(password, MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH)) {
            return true;
        } else {
            throw new ServiceInvalidDataException("Invalid password");
        }
    }

    public static boolean validatePasswordsEquality(byte[] password, byte[] confirmedPassword)
            throws ServiceInvalidDataException {
        if (Arrays.equals(password, confirmedPassword)) {
            return true;
        } else {
           throw new ServiceInvalidDataException("Confirmed password does not" +
                   "match the input password");
        }
    }

    private static boolean checkPasswordLength(byte[] password, int minLength, int maxLength) {
        boolean isValidLength;

        isValidLength = password.length >= minLength && password.length <= maxLength;
        return isValidLength;
    }
}
