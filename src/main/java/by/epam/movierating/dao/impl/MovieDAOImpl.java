package by.epam.movierating.dao.impl;

import by.epam.movierating.bean.Movie;
import by.epam.movierating.dao.MovieDAO;
import by.epam.movierating.dao.connectionpool.ConnectionPool;
import by.epam.movierating.dao.exception.ConnectionPoolException;
import by.epam.movierating.dao.exception.DAOException;

import java.sql.*;
import java.util.*;
import java.util.Date;

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
            "INSERT INTO movie (release_year,movie_duration, age_limit, created_at)" +
                    " VALUES (?,?,?,?)";
    private static final String SQL_ADD_LANGUAGE_DEPENDENT_MOVIE_INFO =
            " INSERT INTO movie_localization(id_movie, language_code, title, description, slogan)" +
                    " VALUES (?,?,?,?,?)";
    private static final String SQL_GET_TOP_MOVIES =
            "SELECT movie.*, m_loc.title, m_loc.description, m_loc.slogan " +
                    "FROM movie " +
                    "JOIN movie_localization AS m_loc ON movie.id = m_loc.id_movie " +
                    "AND m_loc.language_code = ? " +
                    "WHERE movie.deleted_at IS NULL " +
                    "GROUP BY movie.id " +
                    "ORDER BY movie.rating DESC;";
    private static final String SQL_DELETE_MOVIE = "UPDATE movie SET deleted_at = ? WHERE id = ?";
    private static final String SQL_GET_NEWEST_MOVIES =
            "SELECT movie.*, avg(movie_rating.mark) FROM movie\n" +
                    "INNER JOIN movie_rating ON movie.id=movie_rating.id_movie\n" +
                    "GROUP BY movie.id\n" +
                    "ORDER BY movie.release_year DESC;";
    private static final String SQL_UPDATE_MOVIE_POSTER =
            "UPDATE movie SET poster_url = ? WHERE id = ?";
    private static final String SQL_UPDATE_MOVIE_BY_ID =
            "UPDATE movie SET  release_year = ?, movie_duration = ?, age_limit = ?" +
                    " WHERE id = ?";
    private static final String SQL_GET_MOST_DISCUSSED_MOVIES =
            " SELECT movie.*,m_loc.title,m_loc.description,m_loc.slogan" +
                    " FROM movie " +
                    "JOIN movie_localization AS m_loc ON movie.id=m_loc.id_movie " +
                    "AND m_loc.language_code = ? " +
                    "WHERE movie.deleted_at IS NULL " +
                    "GROUP BY movie.id " +
                    "ORDER BY movie.amount_of_reviews DESC;"; //todo

    private static final String SQL_ADD_COUNTRY_FOR_MOVIE =
            " INSERT INTO movie_country(id_movie, country_code) VALUES (?,?)";
    private static final String SQL_DELETE_COUNTRY_FOR_MOVIE =
            " DELETE FROM movie_country WHERE id_movie = ? AND country_code = ?";
    private static final String SQL_ADD_PARTICIPANT_FOR_MOVIE =
            " INSERT INTO movie_participant(id_movie, id_participant,id_movierole) VALUES (?,?,?)";
    private static final String SQL_DELETE_PARTICIPANT_FOR_MOVIE =
            " DELETE FROM movie_participant WHERE id_movie = ?" +
                    " AND id_participant = ?" +
                    " AND id_movierole = ?";
    private static final String SQL_ADD_GENRE_FOR_MOVIE =
            " INSERT INTO movie_genre(id_movie, id_genre) VALUES (?,?)";
    private static final String SQL_DELETE_GENRE_FOR_MOVIE =
            " DELETE FROM movie_genre WHERE id_movie = ? AND id_genre = ?";
    private static final String SQL_CHECK_LANGUAGE_MOVIE_INFO_BY_CODE
            = "SELECT * FROM movie_localization" +
            " WHERE id_movie = ? AND language_code = ?";
    private static final String SQL_GET_LANGUAGE_MOVIE_INFO_BY_CODE =
            " SELECT movie.id,m_loc.title,m_loc.slogan,m_loc.description" +
                    " FROM movie" +
                    " INNER JOIN movie_localization AS m_loc" +
                    " ON movie.id = m_loc.id_movie " +
                    " WHERE id_movie = ? AND language_code = ?";
    private static final String SQL_UPDATE_LANG_DEPENDENT_MOVIE_INFO =
            " UPDATE movie_localization SET title = ?, description = ?, slogan = ?" +
                    " WHERE id_movie = ? AND language_code = ?";


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
            preparedStatement.setInt(1, movie.getReleaseYear());
            preparedStatement.setTime(2, movie.getDuration());
            preparedStatement.setString(3, movie.getAgeLimit());
            preparedStatement.setDate(4, convertJavaDateToSqlDate(movie.getCreatedAt()));
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
    public boolean updateMovie(Movie movie) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_MOVIE_BY_ID);
            preparedStatement.setInt(1, movie.getReleaseYear());
            preparedStatement.setTime(2, movie.getDuration());
            preparedStatement.setString(3, movie.getAgeLimit());
            preparedStatement.setInt(4, movie.getId());
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_MOVIE_BY_ID query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isAdded;
    }

    @Override
    public boolean addLanguageDependentMovieInfo(Movie movie, String contentLanguage)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_LANGUAGE_DEPENDENT_MOVIE_INFO);
            preparedStatement.setInt(1, movie.getId());
            preparedStatement.setString(2, contentLanguage);
            preparedStatement.setString(3, movie.getTitle());
            preparedStatement.setString(4, movie.getDescription());
            preparedStatement.setString(5, movie.getSlogan());
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_LANGUAGE_DEPENDENT_MOVIE_INFO query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isAdded;
    }

    @Override
    public boolean addGenreForMovie(int idMovie, int genreId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_GENRE_FOR_MOVIE);
            preparedStatement.setInt(1, idMovie);
            preparedStatement.setInt(2, genreId);
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_GENRE_FOR_MOVIE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isAdded;
    }

    @Override
    public boolean addCountryForMovie(int movieId, String countryCode) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_COUNTRY_FOR_MOVIE);
            preparedStatement.setInt(1, movieId);
            preparedStatement.setString(2, countryCode);
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_COUNTRY_FOR_MOVIE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isAdded;
    }

    @Override
    public boolean addParticipantForMovie(int idMovie, int idParticipant, int movieroleId)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isAdded;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_ADD_PARTICIPANT_FOR_MOVIE);
            preparedStatement.setInt(1, idMovie);
            preparedStatement.setInt(2, idParticipant);
            preparedStatement.setInt(3, movieroleId);
            isAdded = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_ADD_PARTICIPANT_FOR_MOVIE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isAdded;
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
    public boolean deleteGenreForMovie(int movieId, int genreId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isDeleted;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_GENRE_FOR_MOVIE);
            preparedStatement.setInt(1, movieId);
            preparedStatement.setInt(2, genreId);
            isDeleted = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_DELETE_GENRE_FOR_MOVIE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isDeleted;
    }

    @Override
    public boolean deleteCountryForMovie(int movieId, String countryCode) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isDeleted;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_COUNTRY_FOR_MOVIE);
            preparedStatement.setInt(1, movieId);
            preparedStatement.setString(2, countryCode);
            isDeleted = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_DELETE_COUNTRY_FOR_MOVIE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isDeleted;
    }

    @Override
    public boolean deleteParticipantForMovie(int movieId, int participantId, int movieroleId)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isDeleted;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_PARTICIPANT_FOR_MOVIE);
            preparedStatement.setInt(1, movieId);
            preparedStatement.setInt(2, participantId);
            preparedStatement.setInt(3, movieroleId);
            isDeleted = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection ", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_DELETE_PARTICIPANT_FOR_MOVIE query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isDeleted;
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
            preparedStatement.setDate(1, convertJavaDateToSqlDate(new Date()));
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

    @Override
    public boolean checkLanguageMovieInfoByCode(int movieId, String languageCode)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isExist;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_CHECK_LANGUAGE_MOVIE_INFO_BY_CODE);
            preparedStatement.setInt(1, movieId);
            preparedStatement.setString(2, languageCode);
            resultSet = preparedStatement.executeQuery();
            isExist = resultSet.next();
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_CHECK_LANGUAGE_MOVIE_INFO_BY_CODE query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return isExist;
    }

    @Override
    public Movie getLanguageMovieInfoByCode(int movieId, String languageCode)
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Movie movie;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_LANGUAGE_MOVIE_INFO_BY_CODE);
            preparedStatement.setInt(1, movieId);
            preparedStatement.setString(2, languageCode);
            resultSet = preparedStatement.executeQuery();
            movie = setDataForOneMovie(resultSet);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_GET_LANGUAGE_MOVIE_INFO_BY_CODE query", e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return movie;
    }

    @Override
    public boolean updateLangDependentMovieInfo(Movie movie, String contentLanguage) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isUpdated;
        int oneAffectedRow = 1;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_LANG_DEPENDENT_MOVIE_INFO);
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setString(3, movie.getSlogan());
            preparedStatement.setInt(4, movie.getId());
            preparedStatement.setString(5, contentLanguage);
            isUpdated = (preparedStatement.executeUpdate() == oneAffectedRow);
        } catch (ConnectionPoolException e) {
            throw new DAOException("Can not get a connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error during SQL_UPDATE_LANG_DEPENDENT_MOVIE_INFO query", e);
        } finally {
            close(connection, preparedStatement);
        }
        return isUpdated;
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

    private Movie createMovie(ResultSet rs) throws SQLException {
        Movie movie = new Movie();

        movie.setId(isColumnExist(Column.ID, rs) ?
                rs.getInt(Column.ID) : -1);
        movie.setReleaseYear(isColumnExist(Column.RELEASE_YEAR, rs) ?
                rs.getInt(Column.RELEASE_YEAR) : 0);
        movie.setPosterURL(isColumnExist(Column.POSTER_URL, rs) ?
                rs.getString(Column.POSTER_URL) : null);
        movie.setDuration(isColumnExist(Column.MOVIE_DURATION, rs) ?
                rs.getTime(Column.MOVIE_DURATION) : null);
        movie.setAgeLimit(isColumnExist(Column.AGE_LIMIT, rs) ?
                rs.getString(Column.AGE_LIMIT) : null);
        movie.setRating(isColumnExist(Column.RATING, rs) ?
                rs.getBigDecimal(Column.RATING) : null);
        movie.setAmountOfMarks(isColumnExist(Column.AMOUNT_OF_MARKS, rs) ?
                rs.getInt(Column.AMOUNT_OF_MARKS) : -1);
        movie.setAmountOfReviews(isColumnExist(Column.AMOUNT_OF_REVIEWS, rs) ?
                rs.getInt(Column.AMOUNT_OF_REVIEWS) : -1);
        movie.setDeletedAt(isColumnExist(Column.DELETED_AT, rs) ?
                rs.getDate(Column.DELETED_AT) : null);
        movie.setTitle(isColumnExist(Column.TITLE, rs) ?
                rs.getString(Column.TITLE) : null);
        movie.setDescription(isColumnExist(Column.DESCRIPTION, rs) ?
                rs.getString(Column.DESCRIPTION) : null);
        movie.setSlogan(isColumnExist(Column.SLOGAN, rs) ?
                rs.getString(Column.SLOGAN) : null);
        movie.setCreatedAt(isColumnExist(Column.CREATED_AT, rs) ?
                rs.getDate(Column.CREATED_AT) : null);

        return movie;
    }

    private class Column {
        private static final String ID = "id";
        private static final String RELEASE_YEAR = "release_year";
        private static final String POSTER_URL = "poster_url";
        private static final String MOVIE_DURATION = "movie_duration";
        private static final String AGE_LIMIT = "age_limit";
        private static final String RATING = "rating";
        private static final String AMOUNT_OF_MARKS = "amount_of_marks";
        private static final String AMOUNT_OF_REVIEWS = "amount_of_reviews";
        private static final String DELETED_AT = "deleted_at";
        private static final String TITLE = "title";
        private static final String DESCRIPTION = "description";
        private static final String SLOGAN = "slogan";
        private static final String CREATED_AT = "createdAt";
    }
}
