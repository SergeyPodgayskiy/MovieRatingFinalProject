package by.epam.movierating.service;

import by.epam.movierating.bean.Country;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;

/**
 * @author serge
 *         30.06.2017.
 */
public interface CountryService {
    List<Country> getCountriesByMovieId(int idMovie, String language) throws ServiceException;

    List<Country> getAllCountries(String language) throws ServiceException;

}
