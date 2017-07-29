package by.epam.movierating.dao;

import by.epam.movierating.bean.Country;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.util.ColumnExistable;
import by.epam.movierating.dao.util.DAODefaultFunctional;
import by.epam.movierating.dao.util.JDBCAutocloseable;

import java.util.List;

/**
 * @author serge
 *         02.06.2017.
 */
public interface CountryDAO extends DAODefaultFunctional, JDBCAutocloseable, ColumnExistable {
    boolean addCountry(Country country) throws DAOException;

    List<Country> getAllCountries(String language) throws DAOException;

    List<Country> getCountriesByMovieId(int idMovie, String language) throws DAOException;

    Country getCountryByCode(String countryCode) throws DAOException;

    boolean updateCountry(Country country) throws DAOException;

    boolean deleteCountry(int countryCode) throws DAOException;
}
