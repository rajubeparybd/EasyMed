package com.easymed.utils;

import java.time.LocalTime;

public class GreetingMaker {
    private static final LocalTime MORNING = LocalTime.of(0, 0, 0);

    private static final LocalTime AFTER_NOON = LocalTime.of(12, 0, 0);

    private static final LocalTime EVENING = LocalTime.of(16, 0, 0);

    private static final LocalTime NIGHT = LocalTime.of(21, 0, 0);

    private final LocalTime now;

    /**
     * Create a new GreetingMaker instance with the current time
     */
    public GreetingMaker() {
        this.now = LocalTime.now();
    }

    /**
     * Create a new GreetingMaker instance with the given time
     *
     * @param now LocalTime
     */
    public GreetingMaker(LocalTime now) {
        this.now = now;
    }

    /**
     * Print the greeting of the day based on the current time
     *
     * @return String
     */
    public String printTimeOfDay() {
        if (between(MORNING, AFTER_NOON)) {
            return "Good Morning,";
        } else if (between(AFTER_NOON, EVENING)) {
            return "Good Afternoon,";
        } else if (between(EVENING, NIGHT)) {
            return "Good Evening,";
        } else {
            return "Good Night, ";
        }
    }

    /**
     * Check if the current time is between the given start and end time
     *
     * @param start LocalTime
     * @param end   LocalTime
     *
     * @return boolean
     */
    private boolean between(LocalTime start, LocalTime end) {
        return (!now.isBefore(start)) && now.isBefore(end);
    }

}
