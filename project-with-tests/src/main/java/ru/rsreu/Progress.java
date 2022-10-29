package ru.rsreu;

import java.util.concurrent.locks.ReentrantLock;

public class Progress {
    private final long repeats;
    private long progress;

    private final ReentrantLock reentrantLock = new ReentrantLock();

    public Progress(long repeats) {
        this.repeats = repeats;
    }

    public void updateProgress(long delta) {
        //System.out.println(Thread.currentThread().getName());
        reentrantLock.lock();
        try {
            progress += delta;
            System.out.printf("Calculated %d out of %d\n", progress, repeats);
        } finally {
            reentrantLock.unlock();
        }
    }
}
