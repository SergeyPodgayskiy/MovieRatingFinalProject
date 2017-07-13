package by.epam.movierating.dao.impl;

import by.epam.movierating.bean.Movie;
import by.epam.movierating.dao.MovieDAO;
import by.epam.movierating.dao.connectionpool.ConnectionPool;
import by.epam.movierating.dao.exception.ConnectionPoolException;
import by.epam.movierating.dao.exception.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author serge
 *         02.06.2017.
 */
public class MovieDAOImpl implements MovieDAO {
    private static final String SQL_GET_MOVIE_BY_ID =
            "SELECT movie.*,m_loc.title,m_loc.description,m_loc.slogan " +
                    " FROM movie" +
                    " JOIN movie_localization AS m_loc ON movie.id = m_loc.id_movie " +
                    " AND m_loc.language_code = ?" +
                    " WHERE movie.id=?" +
                    " AND movie.deleted_at IS NULL ";
    private static final String SQL_GET_ALL_MOVIES =
            "SELECT movie.* FROM movie";
    private static final String SQL_ADD_MOVIE =
            "INSERT INTO movie (title, description, release_year, " +
                    "slogan, movie_duration, age_limit) VALUES (?,?,?,?,?,?)";
    private static final String SQL_GET_TOP_MOVIES =
            "SELECT movie.*, m_loc.title, m_loc.description, m_loc.slogan " +
                    "FROM movie " +
                    "JOIN movie_localization AS m_loc ON movie.id = m_loc.id_movie " +
                    "AND m_loc.language_code = ? " +
                    "WHERE movie.deleted_at IS NULL " +
                    "GROUP BY movie.id " +
                    "ORDER BY movie.rating DESC;";
    private static final String SQL_DELETE_MOVIE =
            "UPDATE  movie SET deleted_at = ? WHERE id = ?";
    private static final String SQL_GET_NEWEST_MOVIES =
            "SELECT movie.*, avg(movie_rating.mark) FROM movie\n" +
                    "INNER JOIN movie_rating ON movie.id=movie_rating.id_movie\n" +
                    "GROUP BY movie.id\n" +
                    "ORDER BY movie.release_year DESC;";
    private static final String SQL_UPDATE_MOVIE_POSTER =
            "UPDATE  movie SET poster_url = ? WHERE id = ?";
    private static final String SQL_UPDATE_MOVIE =
            "UPDATE movie SET title=?, description = ?, release_year = ?," +
                    " slogan = ?, movie_duration = ?, age_limit = ?" +
                    " WHERE id = ?";
    private static final String SQL_GET_MOST_DISCUSSED_MOVIES =
            " SELECT movie.*,m_loc.title,m_loc.description,m_loc.slogan" +
                    " FROM movie " +
                    "JOIN movie_localization AS m_loc ON movie.id=m_loc.id_movie " +
                    "AND m_loc.language_code = ? " +
                    "WHERE movie.deleted_at IS NULL " +
                    "GROUP BY movie.id " +
                    "ORDER BY movie.amount_of_reviews DESC;"; //todo

    @Override
    public int addMovie(Movie movie) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int movieId = -1;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_MOVIE,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setDate(3,
                    convertJavaDateToSqlDate(movie.getReleaseYear()));
            preparedStatement.setString(4, movie.getSlogan());
            preparedStatement.setTime(5, movie.getDuration());
            preparedStatement.setString(6, movie.getAgeLimit());
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
            if (isAdded) {
                movieId = returnGeneratedId(preparedStatement);
            } else {
                throw new DAOException("Creating movie failed, no rows added.");
            }
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_MOVIE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return movieId;
    }

    @Override
    public boolean addGenreForMovie(int idMovie, String genreName) throws DAOException {
        return false;
    }

    @Override
    public boolean addCountryForMovie(int idMovie, String countryName) throws DAOException {
        return false;
    }

    @Override
    public boolean addParticipantForMovie(int idMovie, String firstName, String lastName)
            throws DAOException {
        return false;
    }

    @Override
    public List<Movie> getAllMovies() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Movie> movieList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_MOVIES);
            resultSet = preparedStatement.executeQuery();
            movieList = setDataForMovies(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_ALL_MOVIES query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return movieList;
    }

    @Override
    public List<Movie> getTopMovies(String language) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Movie> movieList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_TOP_MOVIES);
            preparedStatement.setString(1, language);
            resultSet = preparedStatement.executeQuery();
            movieList = setDataForMovies(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_TOP_MOVIES query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return movieList;
    }

    @Override
    public List<Movie> getMostDiscussedMovies(String language) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Movie> movieList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_MOST_DISCUSSED_MOVIES);
            preparedStatement.setString(1, language);
            resultSet = preparedStatement.executeQuery();
            movieList = setDataForMovies(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_MOST_DISCUSSED_MOVIES query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return movieList;
    }

    @Override
    public Movie getMovieById(int idMovie, String language) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Movie movie;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_MOVIE_BY_ID);
            preparedStatement.setString(1, language);
            preparedStatement.setInt(2, idMovie);
            resultSet = preparedStatement.executeQuery();
            movie = setDataForOneMovie(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_MOVIE_BY_ID query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return movie;
    }

    @Override
    public List<Movie> getMoviesByGenreId(int idGenre) throws DAOException {
        return null;
    }

    @Override
    public List<Movie> getMoviesByGenreName(String genreName) throws DAOException {
        return null;
    }

    @Override
    public List<Movie> getMoviesByCountryCode(String countryCode) throws DAOException {
        return null;
    }

    @Override
    public List<Movie> getMoviesByCountryName(String countryName) throws DAOException {
        return null;
    }

    @Override
    public List<Movie> getNewestMovies() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Movie> movieList;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_NEWEST_MOVIES);
            resultSet = preparedStatement.executeQuery();
            movieList = setDataForMovies(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_NEWEST_MOVIES query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return movieList;
    }

    @Override
    public boolean updateMovie(Movie movie) throws DAOException { //todo remake
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_MOVIE);
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setDate(3,
                    convertJavaDateToSqlDate(movie.getReleaseYear()));

            preparedStatement.setString(4, movie.getSlogan());
            preparedStatement.setTime(5, movie.getDuration());
            preparedStatement.setString(6, movie.getAgeLimit());
            isUpdated = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_MOVIE query", e);
        } finally {
            //connection.close();
            close(connection, preparedStatement); //todo
        }
        return isUpdated;
    }

    @Override
    public boolean deleteGenreForMovie(int movieId, String genreName) throws DAOException {
        return false;
    }

    @Override
    public boolean deleteCountryForMovie(int movieId, String countryName) throws DAOException {
        return false;
    }

    @Override
    public boolean deleteParticipantForMovie(int movieId, String firstName, String lastName) throws DAOException {
        return false;
    }

    @Override
    public boolean deleteMovie(int idMovie) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isDeleted;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_MOVIE);
            preparedStatement.setInt(1, idMovie);
            isDeleted = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_DELETE_MOVIE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isDeleted;
    }

    @Override
    public boolean uploadMoviePoster(int idMovie, String imgURL) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isPosterUploaded;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_MOVIE_POSTER);
            preparedStatement.setString(1, imgURL);
            preparedStatement.setInt(2, idMovie);
            isPosterUploaded = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_MOVIE_POSTER query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isPosterUploaded;
    }

    private Movie setDataForOneMovie(ResultSet resultSet) throws SQLException {
        Movie movie = null;
        if (resultSet.next()) {
            movie = createMovie(resultSet);
        }
        return movie;
    }

    private List<Movie> setDataForMovies(ResultSet resultSet) throws SQLException {
        List<Movie> movieList = new ArrayList<>();
        while (resultSet.next()) {
            Movie movie = createMovie(resultSet);
            movieList.add(movie);
        }
        return movieList;
    }

    private Movie createMovie(ResultSet resultSet) throws SQLException {
        Movie movie = new Movie();

        movie.setId(resultSet.getInt(1));
        movie.setReleaseYear(resultSet.getDate(2));
        movie.setPosterURL(resultSet.getString(3));
        movie.setDuration(resultSet.getTime(4));
        movie.setAgeLimit(resultSet.getString(5));
        movie.setRating(resultSet.getBigDecimal(6));
        movie.setAmountOfMarks(resultSet.getInt(7));
        movie.setAmountOfReviews(resultSet.getInt(8));
        movie.setDeletedAt(resultSet.getDate(9));
        movie.setTitle(resultSet.getString(10));
        movie.setDescription(resultSet.getString(11));
        movie.setSlogan(resultSet.getString(12));
        return movie;
    }
}
