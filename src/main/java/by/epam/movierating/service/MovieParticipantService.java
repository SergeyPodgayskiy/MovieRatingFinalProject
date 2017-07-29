package by.epam.movierating.service;

import by.epam.movierating.bean.MovieParticipant;
import by.epam.movierating.bean.MovieRole;
import by.epam.movierating.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * @author serge
 *         30.06.2017.
 */
public interface MovieParticipantService {

    Map<MovieParticipant, List<MovieRole>> getMovieParticipantsByMovieId(int idMovie, String language)
            throws ServiceException;


    List<MovieParticipant> getAllParticipantsByRoleId(int roleId, String language)
            throws ServiceException;

    List<MovieParticipant> getParticipantsInMovieByRole(int movieId,
                                                        int roleId,
                                                        String currentLanguage) throws ServiceException;
}
