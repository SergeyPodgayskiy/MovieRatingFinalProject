package by.epam.movierating.controller.listener;

import by.epam.movierating.command.constant.AttributeName;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author serge
 *         08.07.2017.
 */
public class DefaultLanguageListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().
                setAttribute(AttributeName.DEFAULT_LANGUAGE, AttributeName.ENGLISH);
    }


    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
