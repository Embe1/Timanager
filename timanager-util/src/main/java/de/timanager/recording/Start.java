package de.timanager.recording;

import de.timanager.files.CustomFileHandler;
import de.timanager.files.TimeKey;

import java.time.LocalDateTime;

public final class Start {

    public static void main(String[] args) {
        new CustomFileHandler().addTime(LocalDateTime.now(), TimeKey.WORKTIME_START);
    }
}
