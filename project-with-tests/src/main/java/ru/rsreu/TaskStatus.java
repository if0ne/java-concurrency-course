package ru.rsreu;

public enum TaskStatus {
    QUEUE("Waiting to start"),
    RUNNING("Running"),
    FINISHED("Finished"),
    INTERRUPTED("Interrupted");

    private String nameAlias;

    TaskStatus(String nameAlias) {
        this.nameAlias = nameAlias;
    }

    @Override
    public String toString() {
        return nameAlias;
    }
}
