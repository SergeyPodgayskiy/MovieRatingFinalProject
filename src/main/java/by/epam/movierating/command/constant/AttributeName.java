package by.epam.movierating.command.constant;

/**
 * @author serge
 *         28.05.2017.
 */
public final class AttributeName {
    // language attributes
    public static final String USER_LANGUAGE ="userLanguage";
    public static final String DEFAULT_LANGUAGE="defaultLanguage";

    //    general attributes
    public static final String COMMAND = "command";
    public static final String PREVIOUS_PAGE_QUERY = "previousPageQuery"; //todo may be replace
    public static final String ENGLISH="en_EN";
    public static final String RUSSIAN="ru_RU";

    //    user attributes
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String FULL_NAME = "fullName";
    public static final String EMAIL = "email";
    public static final String CONFIRMED_PASSWORD = "confirmPassword";
    public static final String USER_ID = "userId";
    public static final String IS_BANNED = "isBanned";
    public static final String USER_MOVIE_RATING = "userMovieRating";
    public static final String USERS = "users";

    //    movie attributes
    public static final String MOVIES = "movies";
    public static final String MOVIE_ID = "movieId";
    public static final String MOVIE = "movie";

    //    movie participant attributes
    public static final String PARTICIPANTS = "participants";

    //    genre attributes
    public static final String GENRES = "genres";

    //    country attributes
    public static final String COUNTRIES = "countries";

    //    role attributes
    public static final String ROLE = "role";
    public static final String DEFAULT_ROLE = "defaultRole";
    public static final String USER = "user";
    public static final String ADMIN = "admin";
    public static final String GUEST = "guest";

    public static final String ADMIN_STATUS = "adminStatus";
    public static final String BAN_STATUS = "banStatus";
    public static final String IS_DELETED = "isDeleted";
    public static final String PARTICIPANT = "participant";
    public static final String COUNTRY = "country";
    public static final String PARTICIPANT_MOVIE_ROLES = "movieRoles";
    public static final String PARTICIPANT_ID = "participantId";
    public static final String CURRENT_PAGE_NUMBER = "curPageNumber";
    public static final String REVIEWS = "reviews";
}
