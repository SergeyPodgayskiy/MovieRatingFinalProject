package by.epam.movierating.service;

import by.epam.movierating.bean.Movie;
import by.epam.movierating.bean.MovieParticipant;
import by.epam.movierating.bean.MovieRole;
import by.epam.movierating.service.exception.ServiceException;

import java.util.Date;
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

    int addParticipant(Date birthdate) throws ServiceException;

    boolean updateParticipant(int participantId, Date birthdate) throws ServiceException;

    boolean checkLocalizedParticipantInfo(int participantId, String languageCode)
            throws ServiceException;

    MovieParticipant getLocalizedParticipantInfo(int participantId, String languageCode)
            throws ServiceException;

    boolean addLocalizedParticipantInfo(int participantId,
                                        String contentLanguage,
                                        String name,
                                        String surname) throws ServiceException;

    boolean updateLocalizedParticipantInfo(int participantId,
                                           String contentLanguage,
                                           String name,
                                           String surname) throws ServiceException;

    boolean addCountryForParticipant(int participantId, String countryCode)
            throws ServiceException;

    boolean addRoleForParticipant(int participantId, int roleId) throws ServiceException;

    boolean deleteRoleForParticipant(int participantId, int roleId) throws ServiceException;

    MovieParticipant getParticipantById(int participantId, String currentLanguage)
            throws ServiceException;

    List<MovieRole> getParticipantRoles(int participantId, String currentLanguage) throws ServiceException;

    Map<MovieParticipant, List<Movie>> getAllLimitedActors(String currentLanguage, int currentPageNumber)
            throws ServiceException;

    boolean uploadParticipantPhoto(int idParticipant, String fileName) throws ServiceException;

    String getParticipantPhoto(int idParticipant) throws ServiceException;
}
