package by.epam.movierating.command.util;

import by.epam.movierating.command.constant.AttributeName;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author serge
 *         11.07.2017.
 */
public final class DefineLanguageUtil {

    private DefineLanguageUtil() {
    }

    public static String getCurrentLanguage(HttpServletRequest request) {
        Cookie savedLanguage = CookieUtil.getCookie(request, AttributeName.USER_LANGUAGE);

        String currentLanguage =
                (savedLanguage != null) ?
                        savedLanguage.getValue() :
                        (String) request.getSession().getAttribute(AttributeName.DEFAULT_LANGUAGE);

        return currentLanguage;
    }

}
