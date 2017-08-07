package by.epam.movierating.service.impl;

import by.epam.movierating.bean.Country;
import by.epam.movierating.bean.Movie;
import by.epam.movierating.bean.MovieParticipant;
import by.epam.movierating.bean.MovieRole;
import by.epam.movierating.dao.CountryDAO;
import by.epam.movierating.dao.MovieDAO;
import by.epam.movierating.dao.MovieParticipantDAO;
import by.epam.movierating.dao.MovieRoleDAO;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.factory.DAOFactory;
import by.epam.movierating.service.MovieParticipantService;
import by.epam.movierating.service.exception.ServiceException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author serge
 *         30.06.2017.
 */
public class MovieParticipantServiceImpl implements MovieParticipantService {

    @Override
    public Map<MovieParticipant, List<MovieRole>> getMovieParticipantsByMovieId(int idMovie, String language)
            throws ServiceException {
        Map<MovieParticipant, List<MovieRole>> movieParticipantMap = new LinkedHashMap<>();
        List<MovieParticipant> movieParticipantList;
        List<MovieRole> movieRoleList;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO movieParticipantDAO = daoFactory.getMovieParticipantDAO();
            MovieRoleDAO movieRoleDAO = daoFactory.getMovieRoleDAO();
            movieParticipantList = movieParticipantDAO.getMovieParticipantsByMovieId(idMovie, language);
            for (MovieParticipant movieParticipant : movieParticipantList) {
                int idParticipant = movieParticipant.getId();
                movieRoleList =
                        movieRoleDAO.getRolesInMovieByParticipantId(idParticipant, idMovie, language);
                movieParticipantMap.put(movieParticipant, movieRoleList);
            }

        } catch (DAOException e) {
            throw new ServiceException("Error during getting map of movie participants by movieId", e);
        }

        return movieParticipantMap;
    }


    @Override
    public List<MovieParticipant> getAllParticipantsByRoleId(int roleId, String language)
            throws ServiceException {

        List<MovieParticipant> participants;

        DAOFactory daoFactory = DAOFactory.getInstance();
        MovieParticipantDAO movieParticipantDAO = daoFactory.getMovieParticipantDAO();
        try {
            participants = movieParticipantDAO.getAllParticipantsByRoleId(roleId, language);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting participants by roleId", e);
        }

        return participants;
    }

    @Override
    public List<MovieParticipant> getParticipantsInMovieByRole(int movieId,
                                                               int roleId,
                                                               String currentLanguage)
            throws ServiceException {

        List<MovieParticipant> participants;

        DAOFactory daoFactory = DAOFactory.getInstance();
        MovieParticipantDAO movieParticipantDAO = daoFactory.getMovieParticipantDAO();
        try {
            participants =
                    movieParticipantDAO.getParticipantsInMovieByRole(movieId,
                            roleId, currentLanguage);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting participants in movie by roleId", e);
        }

        return participants;
    }

    @Override
    public int addParticipant(Date birthdate) throws ServiceException {
        int participantId;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO movieParticipantDAO = daoFactory.getMovieParticipantDAO();

            MovieParticipant movieParticipant = new MovieParticipant();
            movieParticipant.setBirthDate(birthdate);

            participantId = movieParticipantDAO.addMovieParticipant(movieParticipant);
        } catch (DAOException e) {
            throw new ServiceException("Error during adding participant", e);
        }

        return participantId;
    }

    @Override
    public boolean updateParticipant(int participantId, Date birthdate) throws ServiceException {
        boolean isUpdated;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO movieParticipantDAO = daoFactory.getMovieParticipantDAO();

            MovieParticipant movieParticipant = new MovieParticipant();
            movieParticipant.setId(participantId);
            movieParticipant.setBirthDate(birthdate);

            isUpdated = movieParticipantDAO.updateMovieParticipant(movieParticipant);
        } catch (DAOException e) {
            throw new ServiceException("Error during updating participant", e);
        }

        return isUpdated;
    }

    @Override
    public boolean checkLocalizedParticipantInfo(int participantId,
                                                 String languageCode)
            throws ServiceException {
        boolean isExist;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO participantDAO = daoFactory.getMovieParticipantDAO();
            isExist = participantDAO.checkLocalizedParticipantInfo(participantId, languageCode);
        } catch (DAOException e) {
            throw new ServiceException("Error during checking info for participant", e);
        }

        return isExist;
    }

    @Override
    public MovieParticipant getLocalizedParticipantInfo(int participantId,
                                                        String languageCode)
            throws ServiceException {

        MovieParticipant movieParticipant;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO participantDAO = daoFactory.getMovieParticipantDAO();
            movieParticipant = participantDAO.getLocalizedParticipantInfo(
                    participantId, languageCode);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting language participant info by code", e);
        }

        return movieParticipant;
    }

    @Override
    public boolean addLocalizedParticipantInfo(int participantId,
                                               String contentLanguage,
                                               String name,
                                               String surname) throws ServiceException {

        boolean isAdded;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO participantDAO = daoFactory.getMovieParticipantDAO();
            MovieParticipant participant = new MovieParticipant();
            participant.setId(participantId);
            participant.setName(name);
            participant.setSurname(surname);
            isAdded = participantDAO.addLocalizedParticipantInfo(participant, contentLanguage);
        } catch (DAOException e) {
            throw new ServiceException("Error during adding language dependent participant info", e);
        }

        return isAdded;
    }

    @Override
    public boolean updateLocalizedParticipantInfo(int participantId,
                                                  String contentLanguage,
                                                  String name,
                                                  String surname) throws ServiceException {

        boolean isUpdated;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO participantDAO = daoFactory.getMovieParticipantDAO();
            MovieParticipant participant = new MovieParticipant();
            participant.setId(participantId);
            participant.setName(name);
            participant.setSurname(surname);
            isUpdated = participantDAO.updateLocalizedParticipantInfo(participant, contentLanguage);
        } catch (DAOException e) {
            throw new ServiceException("Error during updating language dependent participant info", e);
        }

        return isUpdated;
    }

    @Override
    public boolean addCountryForParticipant(int participantId, String countryCode)
            throws ServiceException {

        boolean isAdded;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO participantDAO = daoFactory.getMovieParticipantDAO();
            isAdded = participantDAO.addCountryForParticipant(participantId, countryCode);
        } catch (DAOException e) {
            throw new ServiceException("Error during adding country for participant", e);
        }

        return isAdded;
    }

    @Override
    public boolean addRoleForParticipant(int participantId, int roleId)
            throws ServiceException {

        boolean isAdded;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO participantDAO = daoFactory.getMovieParticipantDAO();
            isAdded = participantDAO.addRoleForParticipant(participantId, roleId);
        } catch (DAOException e) {
            throw new ServiceException("Error during adding role for participant", e);
        }

        return isAdded;
    }

    @Override
    public boolean deleteRoleForParticipant(int participantId, int roleId)
            throws ServiceException {

        boolean isDeleted;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO participantDAO = daoFactory.getMovieParticipantDAO();
            isDeleted = participantDAO.deleteRoleForParticipant(participantId, roleId);
        } catch (DAOException e) {
            throw new ServiceException("Error during deleting role for participant", e);
        }

        return isDeleted;
    }

    @Override
    public MovieParticipant getParticipantById(int participantId, String currentLanguage)
            throws ServiceException {

        MovieParticipant participant;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO participantDAO = daoFactory.getMovieParticipantDAO();
            participant = participantDAO.getMovieParticipantById(participantId, currentLanguage);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting Participant by Id", e);
        }

        return participant;
    }

    @Override
    public List<MovieRole> getParticipantRoles(int participantId, String currentLanguage)
            throws ServiceException {
        //todo validate
        List<MovieRole> movieRoles;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieRoleDAO movieRoleDAO = daoFactory.getMovieRoleDAO();
            movieRoles = movieRoleDAO.getMovieRolesByParticipantId(participantId, currentLanguage);
        } catch (DAOException e) {
            throw new ServiceException("Error during getting participant roles", e);
        }

        return movieRoles;
    }

    @Override
    public Map<MovieParticipant, List<Movie>> getAllLimitedActors(String currentLanguage, int currentPageNumber)
            throws ServiceException {

        List<MovieParticipant> actorList;
        Map<MovieParticipant, List<Movie>> actorsMap = new LinkedHashMap<>();
        List<Movie> movieList;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO participantDAO = daoFactory.getMovieParticipantDAO();
            MovieDAO movieDAO = daoFactory.getMovieDAO();
            CountryDAO countryDAO = daoFactory.getCountryDAO();
            actorList = participantDAO.getAllLimitedActors(currentLanguage, currentPageNumber);
            for (MovieParticipant actor : actorList) {
                int idActor = actor.getId();
                movieList =
                        movieDAO.getMoviesByParticipantId(idActor, currentLanguage);
                Country country = actor.getCountry();
                country = countryDAO.getCountryByCode(country.getCode(), currentLanguage);
                actor.setCountry(country);
                actorsMap.put(actor, movieList);
            }
        } catch (DAOException e) {
            throw new ServiceException("Error during getting limited actors with their movies", e);
        }

        return actorsMap;
    }
}
