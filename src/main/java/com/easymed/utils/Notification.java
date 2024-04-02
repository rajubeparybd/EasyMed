package com.easymed.utils;

import org.controlsfx.control.Notifications;

/**
 * Notification class is used to show different types of notifications.
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class Notification {
    /**
     * success method is used to show success notification without title.
     *
     * @param message String
     */
    public static void success(String message) {
        Notifications.create()
                .text(message)
                .showInformation();
    }

    /**
     * success method is used to show success notification with title.
     *
     * @param title   String
     * @param message String
     */
    public static void success(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .showInformation();
    }

    /**
     * error method is used to show error notification without title.
     *
     * @param message String
     */
    public static void error(String message) {
        Notifications.create()
                .text(message)
                .showError();
    }

    /**
     * error method is used to show error notification with title.
     *
     * @param title   String
     * @param message String
     */
    public static void error(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .showError();
    }

    /**
     * warning method is used to show warning notification without title.
     *
     * @param message String
     */
    public static void warning(String message) {
        Notifications.create()
                .text(message)
                .showWarning();
    }

    /**
     * warning method is used to show warning notification with title.
     *
     * @param title   String
     * @param message String
     */
    public static void warning(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .showWarning();
    }

    /**
     * confirm method is used to show confirmation notification without title.
     *
     * @param message String
     */
    public static void confirm(String message) {
        Notifications.create()
                .text(message)
                .showConfirm();
    }

    /**
     * confirm method is used to show confirmation notification with title.
     *
     * @param title   String
     * @param message String
     */
    public static void confirm(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .showConfirm();
    }
}
