package by.epam.movierating.service.impl;

import by.epam.movierating.bean.MovieParticipant;
import by.epam.movierating.bean.ParticipantMovieRole;
import by.epam.movierating.dao.MovieParticipantDAO;
import by.epam.movierating.dao.ParticipantMovieRoleDAO;
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
    public Map<MovieParticipant, List<ParticipantMovieRole>> getMovieParticipantsByMovieId(int idMovie, String language)
            throws ServiceException {
        Map<MovieParticipant, List<ParticipantMovieRole>> movieParticipantMap = new LinkedHashMap<>();
        List<MovieParticipant> movieParticipantList;
        List<ParticipantMovieRole> participantMovieRoleList;
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            MovieParticipantDAO movieParticipantDAO = daoFactory.getMovieParticipantDAO();
            ParticipantMovieRoleDAO participantMovieRoleDAO = daoFactory.getParticipantMovieRoleDAO();
            movieParticipantList = movieParticipantDAO.getMovieParticipantsByMovieId(idMovie,language);
            for (MovieParticipant movieParticipant : movieParticipantList) {
                int idParticipant = movieParticipant.getId();
                participantMovieRoleList =
                        participantMovieRoleDAO.getMovieRolesByParticipantId(idParticipant,language);
                movieParticipantMap.put(movieParticipant, participantMovieRoleList);
            }

        } catch (DAOException e) {
            throw new ServiceException("Error during getting map of movie participants by movieId", e);
        }
        return movieParticipantMap;
    }
}
