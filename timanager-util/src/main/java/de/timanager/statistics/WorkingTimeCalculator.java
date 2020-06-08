package de.timanager.statistics;

import de.timanager.time.TimeKey;
import de.timanager.time.TimeMap;
import de.timanager.time.TimeUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Does calculations for different time informations.
 */
@Slf4j
public final class WorkingTimeCalculator {
    private TimeMap<String, LocalDateTime> timeMap;
    @Getter
    @Deprecated
    private int workingTimeDayMinutes;
    @Getter
    @Deprecated
    private String workingTimeDayHours;
    @Getter
    private String workingTimeMonthHours;

    public WorkingTimeCalculator(TimeMap<String, LocalDateTime> timeMap, Month month) {
        this.timeMap = timeMap;
        workingTimeMonthHours = calculateHoursOfMonth(month);
    }

    public WorkingTimeCalculator(TimeMap<String, LocalDateTime> timeMap, LocalDateTime dateTime) {
        this.timeMap = timeMap;
        workingTimeDayMinutes = calculateWorkingTimeInMinutes(dateTime);
        workingTimeDayHours = convertMinutesToHours(workingTimeDayMinutes);
    }

    /**
     * Calculates the hours of an complete month.
     *
     * @return the amount of hours as an string.
     */
    private  String calculateHoursOfMonth(Month month) {
        List<LocalDateTime> listOfCompleteDayRecords = TimeUtils.getListOfCompleteDayRecords(timeMap, month);
        TimeKey worktimeStart = TimeKey.WORKTIME_START;
        int minutes = 0;

        for (LocalDateTime dateTime : listOfCompleteDayRecords) {
            minutes += calculateWorkingTimeInMinutes(timeMap.get(worktimeStart.generateKey(dateTime)));
        }
        return convertMinutesToHours(minutes);
    }

    /**
     * For only get the same date of different time-map-values.
     *
     * @param localDateTime
     * @return the date of the localdatetime.
     */
    private LocalDate getDateTimeOfTimeMapValue(LocalDateTime localDateTime) {
        timeMap.get(TimeKey.WORKTIME_START.generateKey(localDateTime));
        return null;
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
     * @param ending    the ending time.
     * @return an integer representing the minutes between the two times.
     */
    private int calculateMinutes(LocalDateTime beginning, LocalDateTime ending) {

        if (beginning != null && ending != null)
            return (int) beginning.until(ending, ChronoUnit.MINUTES);
        else
            return 0;
    }
}
