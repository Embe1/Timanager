package de.timanager.statistics;

import de.timanager.files.CustomFileHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
public class Calculator {

    public Calculator() {
        log.info("Calculator started...");
    }

    public void calculateWeekStatistics() {
        log.info("WeekCalculations started...");
    }

    public Object[][] calculateMonthStatistics() {
        log.info("MonthCalculations started...");

        HashMap<String, LocalDateTime> timeMap = new CustomFileHandler().getTimeMap();
        Object[][] array = new Object[4][timeMap.size()];

        for (int i = 0; i < timeMap.size(); i++) {

        }
        return null;
    }

    public void calculateYearStatistics() {
        log.info("YearCalculations started...");
    }
}
