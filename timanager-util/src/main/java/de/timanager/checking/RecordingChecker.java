package de.timanager.checking;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;

@Slf4j
public class RecordingChecker {

    /**
     * Checks, if the recording of the current day has already started.
     *
     * @param file of current month, to check the state of recording (started or stopped).
     * @param start true, if the recording should be started.
     * @return true, if the recording is started, otherwise false.
     */
    public static boolean isRecordingStarted(String file, boolean start) {
        boolean started = false, stopped = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String next, line = reader.readLine();

            for (boolean last = (line == null); !last; line = next) {
                last = ((next = reader.readLine()) == null);

                if (last) {
                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt(i);

                        if (c == ',') {
                            started = true;
                            stopped = false;
                        } else if (c == ';') {
                            started = false;
                            stopped = true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return start ? started : stopped;
    }

    /**
     * Checks, if the string is parseable into a local-time-object.
     *
     * @param time the string, that has to be parsed.
     * @return true, if no exception occurs.
     */
    public static boolean isValidTime(String time) {

        try {
            LocalTime localTime = LocalTime.parse(time);
        } catch (Exception e) {
            log.warn("Time-object not parseable...!");
            return false;
        }

        return true;
    }
}
