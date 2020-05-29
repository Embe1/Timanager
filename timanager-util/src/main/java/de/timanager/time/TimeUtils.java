package de.timanager.time;

import lombok.extern.slf4j.Slf4j;

import java.time.*;

@Slf4j
public class TimeUtils {

    /**
     * Counts the amount of an permitted key in this map.
     *
     * @param timeKey the key to search for.
     * @return
     */
    public static int getCountOfKey(TimeMap<String, LocalDateTime> timeMap, TimeKey timeKey, Month month) {
        int countedEntries = 0;

        for (int i = 1; i < TimeUtils.getDayCountOfSpecificMonth(LocalDate.now()); i++) {
            LocalDateTime value = timeMap.get(timeKey.generateKey(LocalDateTime.of(
                    LocalDate.now().getYear(),
                    month,
                    i,
                    LocalTime.now().getHour(),
                    LocalTime.now().getMinute(),
                    LocalTime.now().getSecond(),
                    LocalTime.now().getNano())));

            if (value != null) {
                countedEntries++;
                log.error(value.toString());
            }
        }
        return countedEntries;
    }

    /**
     * Returns the amount of days of the permitted month.
     * @param month
     * @return
     */
    public final int getDaysOfMonth(Month month) {
        return 0;
    }

    /**
     * Get the number of days in that month
     * @return the amount of days in the permitted month.
     */
    public static int getDayCountOfSpecificMonth(LocalDate date) {
        return YearMonth.of(date.getYear(), date.getMonth()).lengthOfMonth();
    }
}
