package by.epam.movierating.command.util;

import by.epam.movierating.bean.User;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.service.UserService;
import by.epam.movierating.service.exception.ServiceException;
import by.epam.movierating.service.factory.ServiceFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author serge
 *         11.07.2017.
 */
public final class CookieUtil {

    private CookieUtil() {
    }

    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getCurrentLanguage(HttpServletRequest request) {
        Cookie savedLanguage = getCookie(request, AttributeName.USER_LANGUAGE);

        String currentLanguage = (savedLanguage != null) ?
                savedLanguage.getValue() :
                (String) request.getSession().getAttribute(AttributeName.DEFAULT_LANGUAGE);

        return currentLanguage;
    }

    public static String getCurrentRole(HttpServletRequest request) {
        Cookie savedRole = getCookie(request, AttributeName.ROLE);

        String currentRole = (savedRole != null) ?
                savedRole.getValue() :
                (String) request.getSession().getAttribute(AttributeName.ROLE);

        return currentRole;
    }

    public static void deleteCookie(HttpServletResponse response, Cookie... cookies) {
        for (Cookie cookie : cookies) {
            if (cookie != null) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String... cookieName) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            for (String name : cookieName) {
                if (cookie != null && cookie.getName().equals(name)) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static User getCurrentUser(HttpServletRequest request) throws ServiceException {
        Cookie cookieUserId = getCookie(request, AttributeName.USER_ID);
        User user = null;

        if (cookieUserId != null) {
            int idUser = Integer.parseInt(cookieUserId.getValue());
            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            UserService userService = serviceFactory.getUserService();
            user = userService.getUserById(idUser);
        }
        return user;
    }
}
