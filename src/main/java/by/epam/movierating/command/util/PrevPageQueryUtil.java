package by.epam.movierating.command.util;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

/**
 * @author serge
 *         09.07.2017.
 */
public final class PrevPageQueryUtil {

    private PrevPageQueryUtil(){}

    public static String getPrevPageQuery(HttpServletRequest httpServletRequest) {
        String requestURI = httpServletRequest.getRequestURI();
        String currentQuery = buildCurrentQuery(httpServletRequest);

        return buildPrevPageQuery(requestURI, currentQuery);
    }

    private static String buildCurrentQuery(HttpServletRequest httpServletRequest) {
        final char equalSign = '=';
        final char ampersand = '&';
        StringBuilder currentQuery = new StringBuilder();
        LinkedHashMap<String, String[]> queryParametersMap =
                (LinkedHashMap<String, String[]>) httpServletRequest.getParameterMap();

        for (String parameterName : queryParametersMap.keySet()) { //todo may be check for null and redirect to welcomepage
            String[] parameterValues = queryParametersMap.get(parameterName);
            currentQuery.append(parameterName).append(equalSign);
            for (String value : parameterValues) {
                currentQuery.append(value).append(ampersand);
            }
        }
        return String.valueOf(currentQuery);
    }

    private static String buildPrevPageQuery(String requestURI, String currentQuery) {
        final String prevPageQuerySeparator = "previousPageQuery=";
        final char questionSign = '?';
        StringBuilder prevPageQuery = new StringBuilder();

        prevPageQuery.append(requestURI).append(questionSign);
        prevPageQuery.append(StringUtils.substringAfterLast(currentQuery, prevPageQuerySeparator));

        return String.valueOf(prevPageQuery);
    }
}
