package by.epam.movierating.dao;

import by.epam.movierating.bean.MovieRole;
import by.epam.movierating.dao.exception.DAOException;
import by.epam.movierating.dao.util.ColumnExistable;
import by.epam.movierating.dao.util.DAODefaultFunctional;
import by.epam.movierating.dao.util.JDBCAutocloseable;

import java.util.List;

/**
 * @author serge
 *         11.06.2017.
 */
public interface MovieRoleDAO extends DAODefaultFunctional, JDBCAutocloseable, ColumnExistable {

    int addParticipantMovieRole(MovieRole movieRole) throws DAOException;

    MovieRole getParticipantMovieRoleById(int idParticipantMovieRole) throws DAOException;

    boolean updateParticipantMovieRole(MovieRole movieRole) throws DAOException;

    boolean deleteParticipantMovieRole(int idParticipantMovieRole) throws DAOException;

    List<MovieRole> getRolesInMovieByParticipantId(int idParticipant, int idMovie, String language)
            throws DAOException;

    List<MovieRole> getAllMovieRoles(String currentLanguage) throws DAOException;
}
