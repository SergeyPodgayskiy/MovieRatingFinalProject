package by.epam.movierating.service.impl;

import by.epam.movierating.bean.Country;
import by.epam.movierating.dao.CountryDAO;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.factory.DAOFactory;
import by.epam.movierating.service.CountryService;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;

/**
 * @author serge
 *         30.06.2017.
 */
public class CountryServiceImpl implements CountryService {
    @Override
    public List<Country> getCountriesByMovieId(int idMovie, String language) throws ServiceException {
        List<Country> countryList;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            CountryDAO countryDAO = daoFactory.getCountryDAO();
            countryList = countryDAO.getCountriesByMovieId(idMovie, language);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting countries by movieId",e);
        }
        return countryList;
    }
}
