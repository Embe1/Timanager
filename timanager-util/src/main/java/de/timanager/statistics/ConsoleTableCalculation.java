package de.timanager.statistics;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

public class ConsoleTableCalculation {
//    private static final Short stringLength = 12;
//
//    /**
//     * Start the calculation of the Working Times for an given month.
//     *
//     * @param args
//     */
//    public static void main(String[] args) {
//        if (args.length > 0) {
//            WorkingTimeCalculator workingTimeCalculator = new WorkingTimeCalculator(args[0].toUpperCase());
//            new ConsoleTableCalculation(workingTimeCalculator);
//        }
//    }
//
//    public ConsoleTableCalculation(WorkingTimeCalculator workingTimeCalculator) {
//        setupTableHorizontalLine("~");
//        setupTableHeader();
//        setupTableHorizontalLine("~");
//        buildTableContent(workingTimeCalculator.getWorkingtimedayminutes(), workingTimeCalculator.getWorkingTimes());
//        setupTableHorizontalLine("-");
//        buildSumWorkingTime(workingTimeCalculator);
//        setupTableHorizontalLine("-");
//        buildMustSumWorkingTime(workingTimeCalculator);
//        setupTableHorizontalLine("-");
//    }
//
//    private void buildSumWorkingTime(WorkingTimeCalculator workingTimeCalculator) {
//        formatAndPrintString("",
//                " Summe: ",
//                String.valueOf(workingTimeCalculator.getMonthlyMinutesSum()),
//                WorkingTimeCalculator.convertMinutesToHours(workingTimeCalculator.getMonthlyMinutesSum()),
//                "",
//                "");
//    }
//
//    private void buildMustSumWorkingTime(WorkingTimeCalculator workingTimeCalculator) {
//        formatAndPrintString("",
//                " Insgesamt: ",
//                String.valueOf(workingTimeCalculator.getMonthlyMinutesMust()),
//                WorkingTimeCalculator.convertMinutesToHours(workingTimeCalculator.getMonthlyMinutesMust()),
//                "",
//                "");
//    }
//
//    private void setupTableHeader() {
//        formatAndPrintString(" Tag ", " Datum ", " Dauer(m) ", " Dauer(h) ", "Start", "Ende");
//    }
//
//    private void buildTableContent(TreeMap<LocalDate, Integer> dailyTimes, TreeMap<LocalDate, LocalTime[]> workingTimes) {
//        while (!dailyTimes.isEmpty() && !workingTimes.isEmpty()) {
//            LocalDate date = dailyTimes.firstEntry().getKey();
//            Integer minutes = dailyTimes.firstEntry().getValue();
//            String formattedDate = DateTimeFormatter.ofPattern("dd.MM.YYYY").format(date);
//
//            LocalTime start = workingTimes.get(date)[0];
//            LocalTime end = workingTimes.get(date)[1];
//            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//            String formattedStart = timeFormatter.format(start);
//            String formattedEnd = timeFormatter.format(end);
//
//            setupTableContent(date, formattedDate, minutes, formattedStart, formattedEnd);
//            dailyTimes.remove(date);
//            workingTimes.remove(date);
//        }
//    }
//
//    private void setupTableContent(LocalDate date, String formattedDate, Integer minutes, String formattedStart, String formattedEnd) {
//        formatAndPrintString(" " + date.getDayOfWeek().toString() + " ",
//                " " + formattedDate + " ",
//                " " + minutes + " ",
//                " " + WorkingTimeCalculator.convertMinutesToHours(minutes) + " ",
//                " " + formattedStart + " ",
//                " " + formattedEnd + " "
//        );
//    }
//
//    private void setupTableHorizontalLine(String lineString) {
//        StringBuilder stringBuilder = new StringBuilder();
//
//        stringBuilder.append(lineString.repeat(Math.max(0, stringLength)));
//
//        formatAndPrintString(stringBuilder.toString(),
//                stringBuilder.toString(),
//                stringBuilder.toString(),
//                stringBuilder.toString(),
//                stringBuilder.toString(),
//                stringBuilder.toString()
//        );
//    }
//
//    private void formatAndPrintString(String value1, String value2, String value3, String value4, String value5, String value6) {
//        System.out.println(String.format("|%s|%s|%s|%s|%s|%s|",
//                stringPreparation(value1),
//                stringPreparation(value2),
//                stringPreparation(value3),
//                stringPreparation(value4),
//                stringPreparation(value5),
//                stringPreparation(value6)
//        ));
//    }
//
//    private String stringPreparation(String string) {
//        StringBuilder stringBuilder = new StringBuilder(string);
//
//        while (stringBuilder.length() < stringLength) {
//            stringBuilder.append(" ");
//            stringBuilder.insert(0, ' ');
//        }
//
//        if (stringBuilder.length() > stringLength)
//            stringBuilder.deleteCharAt(0);
//
//        return stringBuilder.toString();
//    }
}
