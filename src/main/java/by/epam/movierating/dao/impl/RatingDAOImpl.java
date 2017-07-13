package by.epam.movierating.dao.impl;

import by.epam.movierating.bean.Rating;
import by.epam.movierating.dao.RatingDAO;
import by.epam.movierating.dao.connectionpool.ConnectionPool;
import by.epam.movierating.dao.exception.ConnectionPoolException;
import by.epam.movierating.dao.exception.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author serge
 *         02.06.2017.
 */
public class RatingDAOImpl implements RatingDAO {
    private static final String SQL_RATE_MOVIE =
            "INSERT INTO `movie_rating` (id_movie, id_user, mark) VALUES(?,?,?)";
    private static final String SQL_CHECK_RATE_OPPORTUNITY =
            "SELECT * FROM `movie_rating` WHERE id_movie=? and id_user=?";
    private static final String SQL_DELETE_RATING =
            "DELETE FROM `movie_rating` WHERE id_movie=? and id_user=?";
    private static final String GET_USER_MOVIE_RATING_BY_USER_ID =
            "SELECT * FROM `movie_rating` WHERE id_movie=? AND id_user=?";
    private static final String SQL_UPDATE_MOVIE_RATING =
            "UPDATE movie_rating" +
                    " SET movie_rating.mark = ? " +
                    " WHERE movie_rating.id_movie = ?" +
                    " AND movie_rating.id_user = ? ";

    @Override
    public boolean rateMovie(Rating rating) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isRated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_RATE_MOVIE);
            preparedStatement.setInt(1, rating.getIdMovie());
            preparedStatement.setInt(2, rating.getIdUser());
            preparedStatement.setBigDecimal(3, rating.getMark());
            isRated = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_RATE_MOVIE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isRated;
    }

    @Override
    public boolean updateMovieRating(Rating rating) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdatedRating;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_MOVIE_RATING);
            preparedStatement.setBigDecimal(1, rating.getMark());
            preparedStatement.setInt(2, rating.getIdMovie());
            preparedStatement.setInt(3, rating.getIdUser());
            isUpdatedRating = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_MOVIE_RATING query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isUpdatedRating;
    }

    @Override
    public Rating getUserMovieRatingByUserId(int idMovie, int idUser) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Rating rating = null;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(GET_USER_MOVIE_RATING_BY_USER_ID);
            preparedStatement.setInt(1, idMovie);
            preparedStatement.setInt(2, idUser);
            resultSet = preparedStatement.executeQuery();
            rating = setDataForOneRating(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during GET_USER_MOVIE_RATING_BY_USER_ID query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return rating;
    }

    @Override
    public boolean deleteRating(int movieId, int userId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isDeleted;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_RATING);
            preparedStatement.setInt(1, movieId);
            preparedStatement.setInt(2, userId);
            isDeleted = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_DELETE_RATING query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isDeleted;
    }

    @Override
    public boolean checkRateOpportunity(int idMovie, int idUser) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isValid;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_CHECK_RATE_OPPORTUNITY);
            preparedStatement.setInt(1, idMovie);
            preparedStatement.setInt(2, idUser);
            resultSet = preparedStatement.executeQuery();
            isValid = resultSet.next();
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_CHECK_RATE_OPPORTUNITY query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return isValid;
    }

    private Rating setDataForOneRating(ResultSet resultSet) throws SQLException {
        Rating rating = null;
        if (resultSet.next()) {
            rating = createRating(resultSet);
        }
        return rating;
    }

    private List<Rating> setDataForRatings(ResultSet resultSet) throws SQLException {
        List<Rating> ratingList = new ArrayList<>();
        while (resultSet.next()) {
            Rating rating = createRating(resultSet);
            ratingList.add(rating);
        }
        return ratingList;
    }

    private Rating createRating(ResultSet resultSet) throws SQLException {
        Rating rating = new Rating();
        rating.setIdMovie(resultSet.getInt(1));
        rating.setIdUser(resultSet.getInt(2));
        rating.setMark(resultSet.getBigDecimal(3));
        return rating;
    }
}
