package de.timanager.statistics;

import de.timanager.files.TimeKey;
import de.timanager.files.TimeMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Does calculations for different time informations.
 */
@Slf4j
public final class WorkingTimeCalculator {
    private final TimeMap<String, LocalDateTime> timeMap;
    @Getter
    private int workingtimedayminutes;
    @Getter
    private String workingTimeDayHours;

    public WorkingTimeCalculator(TimeMap<String, LocalDateTime> timeMap, LocalDateTime dateTime) {
        this.timeMap = timeMap;
        workingtimedayminutes = calculateWorkingTimeInMinutes(dateTime);
        workingTimeDayHours = convertMinutesToHours(workingtimedayminutes);
    }

    /**
     * Converts the given minutes to an string representing the minutes in hours with leading zero.
     *
     * @param minutes from which the hours should be generated.
     * @return a string representing the working hours of one day.
     */
    public String convertMinutesToHours(Integer minutes) {
        String floor = String.valueOf(Math.floor((double) minutes / 60));
        floor = floor.substring(0, floor.length() - 2) + "," + minutes % 60;

        if (floor.length() > 1 && floor.charAt(floor.length() - 2) == ',')
            floor = floor.substring(0, floor.length() - 1) + "0" + floor.substring(floor.length() - 1);

        return floor;
    }

    /**
     * Calculates the minutes of one day, with lunch times.
     *
     * @param dateTime the key, of which the times should be calculated.
     * @return the amount of minutes of the day.
     */
    public final int calculateWorkingTimeInMinutes(LocalDateTime dateTime) {
        LocalDateTime startTime = timeMap.get(TimeKey.WORKTIME_START.generateKey(dateTime));
        LocalDateTime endTime = timeMap.get(TimeKey.WORKTIME_END.generateKey(dateTime));
        LocalDateTime lunchStart = timeMap.get(TimeKey.LUNCH_START.generateKey(dateTime));
        LocalDateTime lunchEnd = timeMap.get(TimeKey.LUNCH_END.generateKey(dateTime));

        int firstTime = calculateMinutes(startTime, lunchStart);
        int secondTime = calculateMinutes(lunchEnd, endTime);

        return firstTime + secondTime;
    }

    /**
     * Calculates the differences between two {@link LocalDateTime} objects.
     *
     * @param beginning the beginning time.
     * @param ending the ending time.
     * @return an integer representing the minutes between the two times.
     */
    private int calculateMinutes(LocalDateTime beginning, LocalDateTime ending) {

        if (beginning != null && ending != null)
            return (int) beginning.until(ending, ChronoUnit.MINUTES);
        else
            return 0;
    }
}
