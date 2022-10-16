package ru.rsreu;

public class Progress {
    private final long repeats;
    private long progress;

    public Progress(long repeats) {
        this.repeats = repeats;
    }

    public synchronized void updateProgress(long delta) {
        //System.out.println(Thread.currentThread().getName());
        progress += delta;
        System.out.printf("Calculated %d out of %d\n", progress, repeats);
    }
}
