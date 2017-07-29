package by.epam.movierating.dao.impl;

import by.epam.movierating.bean.Review;
import by.epam.movierating.dao.ReviewDAO;
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
public class ReviewDAOImpl implements ReviewDAO {
    private static final String SQL_ADD_REVIEW =
            "INSERT INTO `review` (id_user, id_movie, publication_date,review_type, title, text)" +
                    "VALUES(?,?,?,?,?,?)";
    private static final String SQL_GET_ALL_REVIEWS_ORDER_BY_DATE =
            "SELECT * FROM `review` " +
                    "ORDER BY publication_date DESC";
    private static final String SQL_DELETE_REVIEW =
            "DELETE FROM `review` WHERE id_user=? and id_movie=?";
    private static final String SQL_CHECK_REVIEW_OPPORTUNITY =
            "SELECT * FROM review WHERE id_user=? and id_movie=?";
    private static final String SQL_GET_REVIEWS_BY_MOVIE_ID =
            "SELECT review.*" +
                    " FROM review " +
                    "WHERE "; //todo

    @Override
    public boolean addReview(Review review) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_REVIEW);
            preparedStatement.setInt(1, review.getIdUser());
            preparedStatement.setInt(2, review.getIdMovie());
            preparedStatement.setDate(3,
                    convertJavaDateToSqlDate(review.getPublicationDate()));
            preparedStatement.setString(4, review.getType());
            preparedStatement.setString(5, review.getTitle());
            preparedStatement.setString(6, review.getText());
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_REVIEW query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isAdded;
    }

    @Override
    public List<Review> getAllReviewsOrderByDate() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Review> reviewList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_REVIEWS_ORDER_BY_DATE);
            resultSet = preparedStatement.executeQuery();
            reviewList = setDataForReviews(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_ALL_REVIEWS_ORDER_BY_DATE query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return reviewList;
    }

    @Override
    public List<Review> getReviewsByMovieId(int idMovie, String language)
            throws DAOException { //todo this query
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Review> reviewList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_REVIEWS_BY_MOVIE_ID);
//            preparedStatement.setString(1,"en_EN");
//            preparedStatement.setInt(2, idMovie);
            resultSet = preparedStatement.executeQuery();
            reviewList = setDataForReviews(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_ALL_REVIEWS_ORDER_BY_DATE query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return reviewList;
    }

    @Override
    public boolean deleteReview(int idMovie, int idUser) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isDeleted;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_REVIEW);
            preparedStatement.setInt(1, idUser);
            preparedStatement.setInt(2, idMovie);
            isDeleted = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_DELETE_REVIEW query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isDeleted;
    }

    @Override
    public boolean checkReviewOpportunity(int idMovie, int idUser) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isValid;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_CHECK_REVIEW_OPPORTUNITY);
            preparedStatement.setInt(1, idUser);
            preparedStatement.setInt(2, idMovie);
            resultSet = preparedStatement.executeQuery();
            isValid = resultSet.next();
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_CHECK_REVIEW_OPPORTUNITY query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return isValid;
    }

    private Review setDataForOneReview(ResultSet resultSet) throws SQLException {
        Review review = null;
        if (resultSet.next()) {
            review = createReview(resultSet);
        }
        return review;
    }

    private List<Review> setDataForReviews(ResultSet resultSet) throws SQLException {
        List<Review> reviewList = new ArrayList<>();
        while (resultSet.next()) {
            Review review = createReview(resultSet);
            reviewList.add(review);
        }
        return reviewList;
    }

    private Review createReview(ResultSet rs) throws SQLException {
        Review review = new Review();

        review.setIdUser(isColumnExist(Column.ID_USER, rs) ?
                rs.getInt(Column.ID_USER) : -1);
        review.setIdMovie(isColumnExist(Column.ID_MOVIE, rs) ?
                rs.getInt(Column.ID_MOVIE) : -1);
        review.setPublicationDate(isColumnExist(Column.PUBLICATION_DATE, rs) ?
                rs.getTimestamp(Column.PUBLICATION_DATE) : null);
        review.setType(isColumnExist(Column.REVIEW_TYPE, rs) ?
                rs.getString(Column.REVIEW_TYPE) : null);
        review.setTitle(isColumnExist(Column.TITLE, rs) ?
                rs.getString(Column.TITLE) : null);
        review.setText(isColumnExist(Column.TEXT, rs) ?
                rs.getString(Column.TEXT) : null);

        return review;
    }

    private class Column {
        private static final String ID_USER = "id_user";
        private static final String ID_MOVIE = "id_movie";
        private static final String PUBLICATION_DATE = "publication_date";
        private static final String REVIEW_TYPE = "review_type";
        private static final String TITLE = "title";
        private static final String TEXT = "text";
    }
}
