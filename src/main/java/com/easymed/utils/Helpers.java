package com.easymed.utils;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * This class contains helper methods that are used in the application
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class Helpers {

    /**
     * Returns the title with the application name appended to it
     *
     * @param title Title of the page
     *
     * @return The title of the page
     */
    public static String getTitle(String title) {
        Dotenv env = Dotenv.load();
        return title + " - " + env.get("APP_NAME");
    }
}
