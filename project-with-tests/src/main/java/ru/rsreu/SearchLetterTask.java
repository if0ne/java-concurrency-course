package ru.rsreu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SearchLetterTask implements Runnable {

    private final ResultContainer container;
    private final String fileName;

    public SearchLetterTask(String fileName, ResultContainer container) {
        this.fileName = fileName;
        this.container = container;
    }

    @Override
    public void run() {
        try {
            search();
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }

    private void search() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (reader.ready()) {
                if (reader.read() == container.getRawLetterRepresentation()) {
                    container.incrementNumberLetter();
                }
            }
        }
    }
}
