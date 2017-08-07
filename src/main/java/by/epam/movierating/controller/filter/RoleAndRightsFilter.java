package by.epam.movierating.controller.filter;

import by.epam.movierating.command.constant.PageName;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.command.util.CookieUtil;
import by.epam.movierating.controller.util.RoleAndCommandsParserUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author serge
 *         11.07.2017.
 */
public class RoleAndRightsFilter implements Filter {
    private static final String WELCOME_PAGE = "show-welcome-page";
    private static final String REDIRECT = "redirect";
    private Map<String, List<String>> roleAndRightsMap;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String command = request.getParameter(ParameterName.COMMAND);
        String role = CookieUtil.getCurrentRole(request);
        roleAndRightsMap = getRoleAndRightsMap(role);
        command = (command != null) ? command : WELCOME_PAGE;

        if (isAllowedCommand(role, command)) {
            if (command.equals(REDIRECT)) {
                if (isAllowedRedirection(request, role)) {
                    filterChain.doFilter(request, response);
                    return;
                } else if (isAdminRedirection(request) && !(isAdmin(role))) {
                    response.sendRedirect(PageName.REDIRECT_TO_WELCOME_PAGE);
                    return;
                } else {
                    response.sendRedirect(PageName.ERROR_404_PAGE);
                    return;
                }
            }
            filterChain.doFilter(request, response);
        } else if (isAdminCommand(command) && !(isAdmin(role))) {
            response.sendRedirect(PageName.REDIRECT_TO_WELCOME_PAGE);
        } else {
            response.sendRedirect(PageName.ERROR_404_PAGE);
        }
    }

    @Override
    public void destroy() {

    }

    private boolean isAllowedCommand(String role, String command) {
        List<String> roleCommands = roleAndRightsMap.get(role);

        for (String commandName : roleCommands) {
            if (commandName.equals(command)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllowedRedirection(HttpServletRequest request, String role) {
        String redirectPage = request.getParameter(ParameterName.REDIRECT_PAGE);
        switch (redirectPage) {
            case ParameterName.REGISTRATION:
                return role.equals(Role.GUEST.toString());
            case ParameterName.ADD_MOVIE_PAGE:
                return role.equals(Role.ADMIN.toString());
            case ParameterName.ADD_PARTICIPANT_PAGE:
                return role.equals(Role.ADMIN.toString());
            case ParameterName.ADD_GENRE_PAGE:
                return role.equals(Role.ADMIN.toString());
            default:
                return false;
        }
    }

    private boolean isAdminCommand(String command) {
        Map<String, List<String>> commandsMap = getRoleAndRightsMap(Role.ADMIN.toString());
        List<String> roleCommands = commandsMap.get(Role.ADMIN.toString());

        for (String commandName : roleCommands) {
            if (commandName.equals(command)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAdminRedirection(HttpServletRequest request) {
        String redirectPage = request.getParameter(ParameterName.REDIRECT_PAGE);
        switch (redirectPage) {
            case ParameterName.ADD_MOVIE_PAGE:
                return true;
            case ParameterName.ADD_PARTICIPANT_PAGE:
                return true;
            case ParameterName.ADD_GENRE_PAGE:
                return true;
            default:
                return false;
        }
    }

    private boolean isAdmin(String role) {
        return role.equals(Role.ADMIN.toString());
    }

    private Map<String, List<String>> getRoleAndRightsMap(String role) {
        return RoleAndCommandsParserUtil.getInstance().getRoleAndCommandsMap(role);
    }

    private enum Role {
        GUEST, USER, ADMIN;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
