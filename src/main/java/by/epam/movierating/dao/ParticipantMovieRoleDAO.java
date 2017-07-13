package by.epam.movierating.dao;

import by.epam.movierating.bean.ParticipantMovieRole;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.util.DAODefaultFunctional;
import by.epam.movierating.dao.util.JDBCAutocloseable;

import java.util.List;

/**
 * @author serge
 *         11.06.2017.
 */
public interface ParticipantMovieRoleDAO extends DAODefaultFunctional, JDBCAutocloseable {
    int addParticipantMovieRole(ParticipantMovieRole participantMovieRole) throws DAOException;

    List<ParticipantMovieRole> getAllParticipantMovieRoles() throws DAOException;

    ParticipantMovieRole getParticipantMovieRoleById(int idParticipantMovieRole) throws DAOException;

    boolean updateParticipantMovieRole(ParticipantMovieRole participantMovieRole) throws DAOException;

    boolean deleteParticipantMovieRole(int idParticipantMovieRole) throws DAOException;

    List<ParticipantMovieRole> getMovieRolesByParticipantId(int idParticipant, String language) throws DAOException;
}
