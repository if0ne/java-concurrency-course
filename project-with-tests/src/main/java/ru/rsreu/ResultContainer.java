package ru.rsreu;

public class ResultContainer {

    private final int trigger;
    private final int rawLetterRepresentation;
    private int numberLetter;

    public ResultContainer(int trigger, char letter) {
        this.trigger = trigger;

        rawLetterRepresentation = letter;
        numberLetter = 0;
    }

    public synchronized void incrementNumberLetter() {
        numberLetter += 1;

        if (numberLetter == trigger) {
            System.out.printf("The summary number of letter '%c' more than %d\n", rawLetterRepresentation, trigger);
        }
    }

    public int getRawLetterRepresentation() {
        return rawLetterRepresentation;
    }

    public synchronized int getNumberLetter() {
        return numberLetter;
    }

}
