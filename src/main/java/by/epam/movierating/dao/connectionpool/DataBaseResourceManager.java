package by.epam.movierating.dao.connectionpool;

import java.util.ResourceBundle;

/**
 * @author serge
 *         09.05.2017.
 */
public final class DataBaseResourceManager {
    private static final DataBaseResourceManager INSTANCE = new DataBaseResourceManager();
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("database");

    private DataBaseResourceManager(){}

    public static DataBaseResourceManager getInstance() {
        return INSTANCE;
    }

    public String getValue(String key) {
        return resourceBundle.getString(key);
    }
}