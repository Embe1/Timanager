package de.timanager.recording;


import de.timanager.files.CustomFileHandler;
import de.timanager.time.TimeKey;

import java.time.LocalDateTime;

public final class Stop {


    public static void main(String[] args) {
        new CustomFileHandler().addTime(LocalDateTime.now(), TimeKey.WORKTIME_END);
    }
}
