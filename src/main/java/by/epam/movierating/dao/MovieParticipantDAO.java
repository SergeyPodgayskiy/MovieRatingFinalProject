package by.epam.movierating.dao;

import by.epam.movierating.bean.MovieParticipant;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.util.ColumnExistable;
import by.epam.movierating.dao.util.DAODefaultFunctional;
import by.epam.movierating.dao.util.JDBCAutocloseable;

import java.util.List;

/**
 * @author serge
 *         02.06.2017.
 */
public interface MovieParticipantDAO extends DAODefaultFunctional, JDBCAutocloseable, ColumnExistable {
    int addMovieParticipant(MovieParticipant movieParticipant) throws DAOException;

    MovieParticipant getMovieParticipantById(int idParticipant, String language) throws DAOException;

    List<MovieParticipant> getMovieParticipantsByMovieId(int idMovie, String language) throws DAOException;

    List<MovieParticipant> getAllMovieParticipants() throws DAOException;

    List<MovieParticipant> getAllParticipantsByRoleId(int roleId, String language) throws DAOException;

    boolean updateMovieParticipant(MovieParticipant movieParticipant) throws DAOException;

    boolean deleteMovieParticipant(int idParticipant) throws DAOException;

    boolean uploadMovieParticipantPhoto(int idParticipant, String photoURL) throws DAOException;


    List<MovieParticipant> getParticipantsInMovieByRole(int movieId,
                                                        int roleId,
                                                        String currentLanguage) throws DAOException;

    boolean checkLocalizedParticipantInfo(int participantId, String languageCode) throws DAOException;

    MovieParticipant getLocalizedParticipantInfo(int participantId, String languageCode)
            throws DAOException;

    boolean addLocalizedParticipantInfo(MovieParticipant participant, String contentLanguage)
            throws DAOException;

    boolean updateLocalizedParticipantInfo(MovieParticipant participant, String contentLanguage)
            throws DAOException;

    boolean addCountryForParticipant(int participantId, String countryCode) throws DAOException;

    boolean addRoleForParticipant(int participantId, int roleId) throws DAOException;

    boolean deleteRoleForParticipant(int participantId, int roleId) throws DAOException;

    List<MovieParticipant> getAllLimitedActors(String currentLanguage, int currentPageNumber)
            throws DAOException;

    String getParticipantPhoto(int idParticipant) throws DAOException;
}
