package de.timanager.files.statisticUtils;

import java.io.IOException;

public final class ReadCsv {

    public static void main(String[] args) throws IOException {
        new Transferrer().readFileContentFromCsv(true);
    }
}
