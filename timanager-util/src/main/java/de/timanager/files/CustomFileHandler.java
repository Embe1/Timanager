package de.timanager.files;

import de.timanager.util.TimeUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashMap;

@Slf4j
public final class CustomFileHandler {
    @Getter
    private static final String path = "/home/michael/IdeaProjects/TiManager/Storings/";
    @Getter
    private static final String month = LocalDate.now().getMonth().toString();
    @Getter
    private static final String year = String.valueOf(LocalDate.now().getYear());
    @Getter
    private static final String postfix = ".ser";
    private static final String storingsPath = path + month + year + postfix;
    @Getter
    private TimeMap<String, LocalDateTime> timeMap;

    /**
     * The initializer for the work with storing-files.
     * Calls the {@link #createFileWithMonthIfNotExist(String)}.
     * Saves the content of this file into an {@link HashMap<String, LocalDateTime>}.
     * And sets all Keynumbers of the {@link TimeKey}-class.
     */
    public CustomFileHandler() {
        this(storingsPath);
    }

    /**
     * The initializer for the work with storing-files.
     * Uses the permitted month to create the file and mappings for this month.
     */
    public CustomFileHandler(Month month) {
        this(path + month.toString() + year + postfix);
    }

    /**
     * The initializer for the work with storing-files.
     * Uses the permitted file to create the file and mappings.
     */
    public CustomFileHandler(String file) {

        try {
            createFileWithMonthIfNotExist(file);
            timeMap = deserializeTime(file);
//            TimeKey.WORKTIME_START.setHighestNumberOfTimeKey(timeMap);
//            TimeKey.WORKTIME_END.setHighestNumberOfTimeKey(timeMap);
//            TimeKey.LUNCH_START.setHighestNumberOfTimeKey(timeMap);
//            TimeKey.LUNCH_END.setHighestNumberOfTimeKey(timeMap);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * The initializer for the work with storing-files.
     * Uses the permitted file to create the file and mappings.
     * Sets also the timeMap with the given one.
     */
    public CustomFileHandler(String file, TimeMap<String, LocalDateTime> tM) {

        try {
            createFileWithMonthIfNotExist(file);
            timeMap = tM;
//            TimeKey.WORKTIME_START.setHighestNumberOfTimeKey(timeMap);
//            TimeKey.WORKTIME_END.setHighestNumberOfTimeKey(timeMap);
//            TimeKey.LUNCH_START.setHighestNumberOfTimeKey(timeMap);
//            TimeKey.LUNCH_END.setHighestNumberOfTimeKey(timeMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the working start time of today, as long as one exist. If no start-time exist, null is returned.
     *
     * @return a {@link LocalTime}-Object of the start-time of today.
     */
    public LocalTime getLastStartWorkTime() {
        if (existWorkTimeStartForToday()) {
            LocalDateTime mapResult = timeMap.get(TimeKey.WORKTIME_START.generateKey(LocalDateTime.now()));
            return mapResult != null ? mapResult.toLocalTime() : LocalTime.now();
        } else {
            log.info("No recording start-time found for today!");
            return LocalTime.of(0, 0);
        }
    }

    /**
     * Returns the lunch time start of today, as long as one exist. If no start-lunch-time exist, null is returned.
     *
     * @return a {@link LocalTime}-Object of the start-time of today.
     */
    public LocalTime getLastStartLunchTime() {
        if (existWorkTimeStartForToday()) {
            LocalDateTime mapResult = timeMap.get(TimeKey.LUNCH_START.generateKey(LocalDateTime.now()));
            return mapResult != null ? mapResult.toLocalTime() : LocalTime.now();

        } else {
            log.info("No recording start-lunch-time found for today!");
            return TimeUtil.getCurrentTimeWithoutNanos();
        }
    }

    /**
     * Returns the lunch time end of today, as long as one exist. If no end-lunch-time exist, null is returned.
     *
     * @return a {@link LocalTime}-Object of the start-time of today.
     */
    public LocalTime getLastEndLunchTime() {
        if (existWorkTimeStartForToday()) {
            LocalDateTime mapResult = timeMap.get(TimeKey.LUNCH_END.generateKey(LocalDateTime.now()));
            return mapResult != null ? mapResult.toLocalTime() : LocalTime.now();
        } else {
            log.info("No recording end-lunch-time found for today!");
            return TimeUtil.getCurrentTimeWithoutNanos();
        }
    }

    /**
     * Returns the worktime end of today, as long as one exist. If no worktime end exist, null is returned.
     *
     * @return a {@link LocalTime}-Object of the start-time of today.
     */
    public LocalTime getLastEndWorkTime() {
        if (existWorkTimeStartForToday()) {
            LocalDateTime mapResult = timeMap.get(TimeKey.WORKTIME_END.generateKey(LocalDateTime.now()));
            return mapResult != null ? mapResult.toLocalTime() : LocalTime.now();
        } else {
            log.info("No recording end-work-time found for today!");
            return TimeUtil.getCurrentTimeWithoutNanos();
        }
    }

    /**
     * Checks, if for today a starting work time exist.
     * If an NPE is thrown, something went really wrong!
     *
     * @return true, if a start-time exist for today.
     */
    public boolean existWorkTimeStartForToday() {
        return timeMap.get(TimeKey.WORKTIME_START.generateKey(LocalDateTime.now())) != null;
//        return timeMap.get(TimeKey.WORKTIME_START.getCurrentKey()).toLocalDate().equals(LocalDate.now());
    }

    /**
     * Checks, if for today a ending work time exist.
     * If an NPE is thrown, something went really wrong!
     *
     * @return true, if a start-time exist for today.
     */
    public boolean existWorkTimeEndForToday() {
        return timeMap.get(TimeKey.WORKTIME_END.generateKey(LocalDateTime.now())) != null;
//        return timeMap.get(TimeKey.WORKTIME_END.getCurrentKey()).toLocalDate().equals(LocalDate.now());
    }

    /**
     * Checks, if for today a starting lunch time exist.
     * If an NPE is thrown, something went really wrong!
     *
     * @return true, if a start-time exist for today.
     */
    public boolean existLunchTimeStartForToday() {
        return timeMap.get(TimeKey.LUNCH_START.generateKey(LocalDateTime.now())) != null;
//        return timeMap.get(TimeKey.LUNCH_START.getCurrentKey()).toLocalDate().equals(LocalDate.now());
    }

    /**
     * Checks, if for today a ending lunch time exist.
     * If an NPE is thrown, something went really wrong!
     *
     * @return true, if a start-time exist for today.
     */
    public boolean existLunchTimeEndForToday() {
        return timeMap.get(TimeKey.LUNCH_END.generateKey(LocalDateTime.now())) != null;
//        return timeMap.get(TimeKey.LUNCH_END.getCurrentKey()).toLocalDate().equals(LocalDate.now());
    }

    /**
     * Adds a {@link LocalDateTime}-object to the {@link #timeMap} and serializes it, if the storing is valid.
     *
     * @param localDateTime the value(time) to store.
     * @param toAddKey the key, according the time to store.
     * @return true, if the time is saved in the file, otherwise false.
     */
    public final boolean addTime(LocalDateTime localDateTime, TimeKey toAddKey) {
        try {
            if (serializeIfValid(localDateTime, toAddKey)) {
                log.info("Folgender Wert wurde gespeichert: " + localDateTime + " mit Key: " + toAddKey);
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public final void serializeForUpdate() throws IOException {
        serializeTime(storingsPath);
    }

    /**
     * Stores the content of the current working times as serialized file.
     *
     * @param fileString is the data to store.
     * @throws IOException if the {@link FileOutputStream} or writeObject()-method runs into complications.
     */
    private void serializeTime(String fileString) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileString))) {
            out.writeObject(timeMap);
        }
    }

    /**
     * Stores the content of the file in a {@link HashMap<String, LocalDateTime>}.
     *
     * @param fileString is the data to store.
     * @throws IOException            if the {@link FileInputStream}-method runs into complications.
     * @throws ClassNotFoundException if the {@link ObjectInputStream}-class cannot read the data from the input-stream.
     */
    private TimeMap<String, LocalDateTime> deserializeTime(String fileString) throws IOException, ClassNotFoundException {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileString))) {
            Object readObject = in.readObject();
            return new TimeMap<>(readObject);
        } catch (EOFException eof) {
            log.info("Empty file loaded.");
            return new TimeMap<>(new Object());
        }
    }

    /**
     * Reads the file from the specified folder. If no file is exist, a new file is created.
     * The name of the file is the name of the month in uppercase letter with the year appended.
     * Example: JANUARY2020.csv
     *
     * @param fileString the path and name of the file.
     * @throws IOException if the creation fails.
     */
    void createFileWithMonthIfNotExist(String fileString) throws IOException {
        File file = new File(fileString);

        if (file.createNewFile()) {
            log.info(String.format("File %s is created!", file.getName()));
        } else {
            log.info(String.format("File %s already exists!", file.getName()));
        }
    }

    /**
     * Serializes the {@link LocalDateTime}-object, if the storing is valid.
     * Add start -> start-count(add) = end-count(test)
     * Add end   -> start-count(add) > end-count(test)
     *
     * @param localDateTime the time to store.
     * @param toAddKey      the value, the time is associated.
     * @throws IOException if the {@link #serializeTime(String)}-method returns any complications.
     */
    private boolean serializeIfValid(LocalDateTime localDateTime, TimeKey toAddKey) throws IOException {

        if (toAddKey.equals(TimeKey.WORKTIME_START) || toAddKey.equals(TimeKey.LUNCH_START)) {

            if (localDateTime.toLocalDate().equals(LocalDate.now())) {
                timeMap.put(toAddKey.generateKey(localDateTime), localDateTime);
                serializeTime(storingsPath);
                return true;

            } else {
                log.warn(toAddKey + " is stored already or not allowed!");
                return false;
            }

        } else {

            if (localDateTime.toLocalDate().equals(LocalDate.now())) {
                timeMap.put(toAddKey.generateKey(localDateTime), localDateTime);
                serializeTime(storingsPath);
                return true;

            } else {
                log.warn(toAddKey + " recording isn't started or stopping is not allowed!");
                return false;
            }
        }
    }
}
