package de.timanager.util;

import java.time.LocalTime;

public final class TimeUtil {

    /**
     * Returns the current time, but without nanoseconds.
     *
     * @return a local time object representing the current time.
     */
    public static LocalTime getCurrentTimeWithoutNanos() {
        return LocalTime.of(
                LocalTime.now().getHour(),
                LocalTime.now().getMinute(),
                LocalTime.now().getSecond());
    }
}
