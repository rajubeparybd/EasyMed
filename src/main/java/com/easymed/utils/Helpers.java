package com.easymed.utils;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     * @return The title of the page
     */
    public static String getTitle(String title) {
        Dotenv env = Dotenv.load();
        return title + " - " + env.get("APP_NAME");
    }

    /**
     * Returns the current date in the default pattern
     *
     * @return The current date
     */
    public static String getDate() {
        return getDate(null);
    }

    /**
     * Returns the current date in the specified pattern
     *
     * @param pattern Date pattern
     * @return The current date
     */
    public static String getDate(String pattern) {
        if (pattern == null) pattern = "EEEE, dd MMMM yyyy";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }

    /**
     * Returns the current time in the default pattern
     *
     * @return The current time
     */
    public static String getTime() {
        return getTime(null);
    }

    /**
     * Returns the current time in the specified pattern
     *
     * @param pattern Time pattern
     * @return The current time
     */
    public static String getTime(String pattern) {
        if (pattern == null) pattern = "hh:mm a";
        DateFormat timeFormat = new SimpleDateFormat(pattern);
        return timeFormat.format(new Date());
    }

    /**
     * Toggles the active class on the sidebar buttons
     *
     * @param actionEvent Button click event
     */
    public static void toggleMenuClass(ActionEvent actionEvent) {
        Node btn = (Node) actionEvent.getSource();
        VBox sidebar = (VBox) btn.getParent();
        for (Node node : sidebar.getChildren()) {
            node.getStyleClass().remove("sidebarBtn-active");
            node.getStyleClass().add("sidebarBtn");
        }
        btn.getStyleClass().remove("sidebarBtn");
        btn.getStyleClass().add("sidebarBtn-active");
    }

    /**
     * Toggles the active class on the sidebar buttons
     *
     * @param sidebar sidebar container
     * @param id      id of the button that was clicked
     */
    public static void toggleMenuClass(VBox sidebar, String id) {
        sidebar.getChildren().forEach(node -> {
            if (node.getId().equals(id) && !node.getStyleClass().contains("sidebarBtn-active")) {
                node.getStyleClass().add("sidebarBtn-active");
                node.getStyleClass().remove("sidebarBtn");
            } else {
                node.getStyleClass().remove("sidebarBtn-active");
                node.getStyleClass().add("sidebarBtn");
            }
        });
    }

    /**
     * Check the json is valid or not
     *
     * @param json JSON
     * @return boolean
     */
    public static boolean isValidJson(String json) {
        try {
            new JSONObject(json);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}
