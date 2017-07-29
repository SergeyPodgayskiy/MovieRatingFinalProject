package by.epam.movierating.service.impl;

import by.epam.movierating.bean.MovieParticipant;
import by.epam.movierating.bean.MovieRole;
import by.epam.movierating.dao.MovieParticipantDAO;
import by.epam.movierating.dao.MovieRoleDAO;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.factory.DAOFactory;
import by.epam.movierating.service.MovieParticipantService;
import by.epam.movierating.service.exception.ServiceException;

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
}
