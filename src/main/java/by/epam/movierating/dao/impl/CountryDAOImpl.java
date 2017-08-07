package by.epam.movierating.dao.impl;

import by.epam.movierating.bean.Country;
import by.epam.movierating.dao.CountryDAO;
import by.epam.movierating.dao.connectionpool.ConnectionPool;
import by.epam.movierating.dao.exception.ConnectionPoolException;
import by.epam.movierating.dao.exception.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author serge
 *         02.06.2017.
 */
public class CountryDAOImpl implements CountryDAO {
    private static final String SQL_ADD_COUNTRY_CODE =
            "INSERT INTO country (code) VALUES (?)";
    private static final String SQL_ADD_LOCALIZED_COUNTRY_DATA =
            "INSERT INTO country_localization (country_code, language_code, name)" +
                    " VALUES ((SELECT country.code FROM country WHERE country.code = ?),?, ? )";
    private static final String SQL_GET_ALL_COUNTRIES =
            "SELECT country.code, country.icon_url, country_localization.name FROM country" +
                    " INNER JOIN country_localization" +
                    " ON country.code = country_localization.country_code" +
                    " AND country_localization.language_code = ?" +
                    " ORDER BY country_localization.name";
    private static final String SQL_GET_ALL_DELETED_COUNTRIES =
            "SELECT country.code, country_localization.name, country.deleted_at FROM country" +
                    " JOIN country_localization" +
                    " ON country.code = country_localization.country_code" +
                    " AND country_localization.language_code = ?" +
                    " WHERE deleted_at IS NOT NULL" +
                    " ORDER BY country_localization.name";
    private static final String SQL_GET_COUNTRY_BY_CODE =
            "SELECT country.*, country_localization.name" +
                    " FROM country" +
                    " JOIN country_localization" +
                    " ON country.code = country_localization.country_code" +
                    " AND country_localization.language_code = ?" +
                    " WHERE code = ?";
    private static final String SQL_GET_ALL_COUNTRIES_BY_CODE = //todo rename
            "SELECT country.code, country_localization.name FROM country" +
                    " JOIN country_localization" +
                    " ON country.code = country_localization.country_code" +
                    " WHERE code = ?" +
                    " AND deleted_at IS NULL" +
                    " ORDER BY country_localization.name";
    private static final String SQL_UPDATE_COUNTRY =
            "UPDATE country_localization SET country_localization.name = ?" +
                    " WHERE country_localization.country_code = ?" +
                    " AND country_localization.language_code = ?";
    private static final String SQL_DELETE_COUNTRY =
            "UPDATE country SET deleted_at = ? WHERE code = ?";
    private static final String SQL_GET_COUNTRIES_BY_MOVIE_ID =
            "SELECT country.*, country_loc.name " +
                    "FROM country " +
                    "INNER JOIN country_localization AS country_loc ON country.code = country_loc.country_code " +
                    "INNER JOIN movie_country ON country.code = movie_country.country_code " +
                    "AND country_loc.language_code = ?" +
                    "WHERE movie_country.id_movie = ?";

    @Override
    public boolean addCountry(Country country) throws DAOException {
        Connection connection = null;
        boolean isAdded;
        int oneAddedCountry = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();

            connection.setAutoCommit(false);
            try (PreparedStatement addCodePreparedStatement =
                         connection.prepareStatement(SQL_ADD_COUNTRY_CODE)) {
                addCodePreparedStatement.setString(1, country.getCode());
                addCodePreparedStatement.executeUpdate();
            }
            try (PreparedStatement addNamePreparedStatement =
                         connection.prepareStatement(SQL_ADD_LOCALIZED_COUNTRY_DATA)) {
                addNamePreparedStatement.setString(1, country.getCode());
                addNamePreparedStatement.setString(2, "en"); //todo insert current language
                addNamePreparedStatement.setString(3, country.getName());
                isAdded = (addNamePreparedStatement.executeUpdate() == oneAddedCountry);
            }
            connection.commit();
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_COUNTRY query", e);
        } finally {
            close(connection);
        }
        return isAdded;
    }



    @Override
    public List<Country> getAllCountries(String language) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Country> countryList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_COUNTRIES);
            preparedStatement.setString(1, language);
            resultSet = preparedStatement.executeQuery();
            countryList = setDataForCountries(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_ALL_COUNTRIES query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return countryList;
    }

    @Override
    public List<Country> getCountriesByMovieId(int idMovie, String language) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Country> countryList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_COUNTRIES_BY_MOVIE_ID);
            preparedStatement.setString(1, language);
            preparedStatement.setInt(2, idMovie);
            resultSet = preparedStatement.executeQuery();
            countryList = setDataForCountries(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_COUNTRIES_BY_MOVIE_ID query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return countryList;
    }

    @Override
    public Country getCountryByCode(String countryCode, String language) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Country country = null;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_COUNTRY_BY_CODE);
            preparedStatement.setString(1, language);
            preparedStatement.setString(2, countryCode);
            resultSet = preparedStatement.executeQuery();
            country = setDataForOneCountry(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection from pool", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_COUNTRY_BY_CODE query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return country;
    }

    @Override
    public boolean updateCountry(Country country) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_COUNTRY);
            preparedStatement.setString(1, country.getName());
            preparedStatement.setString(2, country.getCode());
            isUpdated = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_COUNTRY query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteCountry(int countryCode) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isDeleted;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_COUNTRY);
            preparedStatement.setInt(1, countryCode);
            isDeleted = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_DELETE_COUNTRY query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isDeleted;
    }

    private Country setDataForOneCountry(ResultSet resultSet) throws SQLException {
        Country country = null;
        if (resultSet.next()) {
            country = createCountry(resultSet);
        }
        return country;
    }

    private List<Country> setDataForCountries(ResultSet resultSet) throws SQLException {
        List<Country> countryList = new ArrayList<>();
        while (resultSet.next()) {
            Country country = createCountry(resultSet);
            countryList.add(country);
        }
        return countryList;
    }

    private Country createCountry(ResultSet rs) throws SQLException {
        Country country = new Country();

        country.setCode(isColumnExist(Column.CODE, rs) ?
                rs.getString(Column.CODE) : null);
        country.setIconURL(isColumnExist(Column.ICON_URL, rs) ?
                rs.getString(Column.ICON_URL) : null);
        country.setName(isColumnExist(Column.NAME, rs) ?
                rs.getString(Column.NAME) : null);

        return country;
    }

    private class Column {
        private static final String CODE = "code";
        private static final String ICON_URL = "icon_url";
        private static final String NAME = "name";
    }
}
