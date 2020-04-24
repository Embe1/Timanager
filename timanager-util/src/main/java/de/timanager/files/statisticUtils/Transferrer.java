package de.timanager.files.statisticUtils;

import de.timanager.files.CustomFileHandler;
import de.timanager.files.TimeKey;
import de.timanager.files.TimeMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Only a helper class, for the .csv and .ser files and datas.
 */
@Slf4j
public final class Transferrer {
    @Getter
    private static final String path = "/home/michael/IdeaProjects/TiManager/Storings/";
    @Getter
    private static final String month = "APRIL";
    @Getter
    private static final String year = String.valueOf(LocalDate.now().getYear());
    private static final String pathString = path + month + year;

    /**
     * Reads the content of a file, and saves it into an String.
     * Optionally saves the data to an csv file.
     * Only for transferring data from .ser file to the {@link HashMap <String, LocalDateTime>}.
     *
     * @throws IOException if the readString()-method of the {@link Files}-class returns any complications.
     */
    public void readFileContentFromSer(boolean saveToCsv) throws IOException {
        TimeMap<String, LocalDateTime> localTimeMap = new CustomFileHandler(pathString + ".ser").getTimeMap();

        if (saveToCsv) {

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathString + ".csv"))) {

                for (int i = 0; i < localTimeMap.countDateEntries(); i++) {

                    for (int j = 1; j < LocalDateTime.MAX.getDayOfMonth(); j++) {
                        LocalDateTime dateTime = LocalDateTime.of(
                                LocalDate.now().getYear(),
                                LocalDate.now().getMonth(),
                                i,
                                0,
                                0,
                                0);

                        try {
                            LocalDateTime wts = localTimeMap.get(TimeKey.WORKTIME_START.generateKey(dateTime));
                            LocalDateTime wte = localTimeMap.get(TimeKey.WORKTIME_START.generateKey(dateTime));
                            LocalDateTime ls = localTimeMap.get(TimeKey.WORKTIME_START.generateKey(dateTime));
                            LocalDateTime le = localTimeMap.get(TimeKey.WORKTIME_START.generateKey(dateTime));

                            bufferedWriter.append(String.valueOf(wts)).append(",");
                            bufferedWriter.append(String.valueOf(wte)).append(",");
                            bufferedWriter.append(String.valueOf(ls)).append(",");
                            bufferedWriter.append(String.valueOf(le)).append(";").append(System.lineSeparator());

                        } catch (IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            log.info("Saved to .csv file successfully!");
        }
    }

    /**
     * Reads the content of a file, and saves it into an String.
     * Only for transferring data from .csv file to the {@link HashMap <String,  LocalDateTime >}.
     *
     * @throws IOException if the readString()-method of the {@link Files}-class returns any complications.
     */
    public void readFileContentFromCsv(boolean saveToSer) throws IOException {
        HashMap<String, LocalDateTime> tmpMap = new HashMap<>();
        String content = Files.readString(Paths.get(pathString + ".csv"));
        StringBuilder stringBuilder = new StringBuilder();
        int colCount = 0;

        for (int i = 0; i < content.length(); i++) {

            if (content.charAt(i) == ',' && colCount == 0) {
                String nextKey = TimeKey.WORKTIME_START.generateKey(LocalDateTime.parse(stringBuilder.toString()));
                LocalDateTime localDateTime = LocalDateTime.parse(stringBuilder.toString());

                tmpMap.put(nextKey, localDateTime);
                stringBuilder.replace(0, stringBuilder.length(), "");
                colCount++;

            } else if (content.charAt(i) == ',' && colCount == 1) {
                String nextKey = TimeKey.WORKTIME_END.generateKey(LocalDateTime.parse(stringBuilder.toString()));
                LocalDateTime localDateTime = LocalDateTime.parse(stringBuilder.toString());

                tmpMap.put(nextKey, localDateTime);
                stringBuilder.replace(0, stringBuilder.length(), "");
                colCount++;

            } else if (content.charAt(i) == ',' && colCount == 2) {
                String nextKey = TimeKey.LUNCH_START.generateKey(LocalDateTime.parse(stringBuilder.toString()));
                LocalDateTime localDateTime = LocalDateTime.parse(stringBuilder.toString());

                tmpMap.put(nextKey, localDateTime);
                stringBuilder.replace(0, stringBuilder.length(), "");
                colCount++;

            } else if (content.charAt(i) == ';') {
                String nextKey = TimeKey.LUNCH_END.generateKey(LocalDateTime.parse(stringBuilder.toString()));
                LocalDateTime localDateTime = LocalDateTime.parse(stringBuilder.toString());

                tmpMap.put(nextKey, localDateTime);
                stringBuilder.replace(0, stringBuilder.length(), "");
                colCount++;

            } else if (content.charAt(i) == '\r' || content.charAt(i) == '\n') {
                stringBuilder.replace(0, stringBuilder.length(), "");
                colCount = 0;

            } else {
                stringBuilder.append(content.charAt(i));
            }
        }
        log.info("Data from " + month + " .csv successfully readed!");


        if (saveToSer) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(pathString + ".ser"))) {
                out.writeObject(tmpMap);
                log.info("Saved to .ser file successfully!");
            }
        }
    }
}
