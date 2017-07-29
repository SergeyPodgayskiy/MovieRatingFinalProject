package by.epam.movierating.controller.filter;

import by.epam.movierating.bean.User;
import by.epam.movierating.command.constant.AttributeName;
import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.util.CookieUtil;
import by.epam.movierating.service.exception.ServiceException;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author serge
 *         13.07.2017.
 */
public class CurrentUserFilter implements Filter {
    private static final Logger logger = Logger.getLogger(CurrentUserFilter.class);
    private static final String ROLE = "role";
    private static final String USER_ID = "userId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            User currentUser = CookieUtil.getCurrentUser(request);
            HttpSession session = request.getSession();
            if (currentUser != null) {
                if (currentUser.isAdmin()) {
                    CookieUtil.deleteCookie(request, response, USER_ID, ROLE);
//                    session.setMaxInactiveInterval(300);
                }
                session.setAttribute(AttributeName.USER, currentUser);
                session.setAttribute(AttributeName.USER_ID, currentUser.getId());
                session.setAttribute(AttributeName.IS_BANNED, currentUser.isBanned());
            }
            chain.doFilter(request, response);
        } catch (ServiceException e) {
            logger.error("Error during doing user filter", e);
            response.sendRedirect(PageName.ERROR_500_PAGE);
        }
    }

    @Override
    public void destroy() {

    }
}
