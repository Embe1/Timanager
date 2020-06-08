package de.timanager.time;

import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TimeUtils {

    public static List<LocalDateTime> getListOfCompleteDayRecords(TimeMap<String, LocalDateTime> timeMap, Month month) {
        List<LocalDateTime> worktimeStart = getAllTimesOfTimeKey(timeMap, TimeKey.WORKTIME_START, month);
        List<LocalDateTime> worktimeEnd = getAllTimesOfTimeKey(timeMap, TimeKey.WORKTIME_END, month);
        List<LocalDateTime> lunchtimeStart = getAllTimesOfTimeKey(timeMap, TimeKey.LUNCH_START, month);
        List<LocalDateTime> lunchtimeEnd = getAllTimesOfTimeKey(timeMap, TimeKey.LUNCH_END, month);

        if (worktimeStart.size() <= worktimeEnd.size() && worktimeStart.size() <= lunchtimeStart.size() && worktimeStart.size() <= lunchtimeEnd.size())
            return worktimeStart;
        else if (worktimeEnd.size() <= worktimeStart.size() && worktimeEnd.size() <= lunchtimeStart.size() && worktimeEnd.size() <= lunchtimeEnd.size())
            return worktimeEnd;
        else if (lunchtimeStart.size() <= worktimeStart.size() && lunchtimeStart.size() <= worktimeEnd.size() && lunchtimeStart.size() <= lunchtimeEnd.size())
            return lunchtimeStart;
        else
            return lunchtimeEnd;
    }

    /**
     * Counts the amount of an permitted key in this map.
     *
     * @param timeKey the key to search for.
     * @return a list, with all times, found in the actual month-time-map.
     */
    private static List<LocalDateTime> getAllTimesOfTimeKey(TimeMap<String, LocalDateTime> timeMap, TimeKey timeKey, Month month) {
        List<LocalDateTime> list = new ArrayList<>();
        LocalDate localDate = LocalDate.of(LocalDate.now().getYear(), month, LocalDate.now().getDayOfMonth());

        for (int i = 1; i < TimeUtils.getDayCountOfSpecificMonth(localDate); i++) {
            LocalDateTime value = timeMap.get(
                    timeKey.generateKey(
                            LocalDateTime.of(
                                    LocalDate.now().getYear(),
                                    month,
                                    i,
                                    LocalTime.now().getHour(),
                                    LocalTime.now().getMinute(),
                                    LocalTime.now().getSecond(),
                                    LocalTime.now().getNano()
                            )
                    )
            );

            if (value != null) {
                list.add(value);
            }
        }
        return list;
    }

    /**
     * Get the number of days in that month
     *
     * @return the amount of days in the permitted month.
     */
    public static int getDayCountOfSpecificMonth(LocalDate date) {
        return YearMonth.of(date.getYear(), date.getMonth()).lengthOfMonth();
    }

    /**
     * Returns the amount of days of the permitted month.
     *
     * @param month
     * @return
     */
    public final int getDaysOfMonth(Month month) {
        return 0;
    }
}
