package ru.rsreu;

public class ResultContainer implements Container<Integer> {

    private final int trigger;
    private final int rawLetterRepresentation;
    private int numberLetter;

    public ResultContainer(int trigger, char letter) {
        this.trigger = trigger;

        rawLetterRepresentation = letter;
        numberLetter = 0;
    }

    @Override
    public synchronized void incrementValue() {
        numberLetter += 1;

        if (numberLetter == trigger) {
            System.out.printf("The summary number of letter '%c' more than %d\n", rawLetterRepresentation, trigger);
        }
    }

    @Override
    public synchronized Integer getValue() {
        return numberLetter;
    }

    public int getRawLetterRepresentation() {
        return rawLetterRepresentation;
    }

}
