/*
package by.epam.movierating.dao.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

*/
/**
 * @author serge
 *         11.07.2017.
 *//*

public interface Localizable {

    default String localizeStatement(String sqlQuery, String language) {
        final String resultPattern = "language_code='" + language + "'";
        String searchPattern = "language_code(\\S|\\s*)=(\\S|\\s*)\\?";
        Pattern localizedStringPattern = Pattern.compile(searchPattern);
        Matcher localizedStringMatcher = localizedStringPattern.matcher(sqlQuery);

        if(localizedStringMatcher.find()) {
            sqlQuery = localizedStringMatcher.replaceAll(resultPattern);
            System.out.println(sqlQuery);
        }

        return sqlQuery;
    }
}
*/
