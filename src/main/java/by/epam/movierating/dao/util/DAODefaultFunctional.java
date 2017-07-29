package by.epam.movierating.dao.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author serge
 *         28.05.2017.
 */
public interface DAODefaultFunctional {

    default java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

  /*  default java.sql.Date getYearFromJavaDate(java.util.Date date) {
        LocalDate localDate = date.to
    }*/

    /*default java.sql.Timestamp convertJavaDateToSqlFullDate(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }*/

    default int returnGeneratedId(PreparedStatement preparedStatement)
            throws SQLException {
        int generatedId = -1;
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }
        }
        return generatedId;
    }
}
