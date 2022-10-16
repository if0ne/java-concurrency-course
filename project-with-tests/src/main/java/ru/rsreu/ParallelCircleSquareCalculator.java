package ru.rsreu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelCircleSquareCalculator {

    private final double radius;
    private final long repeats;
    private final int poolSize;
    private final ExecutorService executorService;
    private volatile Progress progress;

    public ParallelCircleSquareCalculator(double radius, long repeats, int poolSize) {
        this.radius = radius;
        this.repeats = repeats;
        this.poolSize = poolSize;
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    public double calculateSquare() {
        long repeatsPerTask = repeats / poolSize;

        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < poolSize; ++i) {
            MonteCarloFuture task = new MonteCarloFuture(repeatsPerTask, 8, this);
            futures.add(executorService.submit(task));
        }

        long inCircle = 0;
        for (Future<Long> future : futures) {
            try {
                inCircle += future.get(10, TimeUnit.MINUTES);
            } catch (Exception ignored) {
            }
        }

        executorService.shutdown();
        double pi = 4.0 * inCircle / repeats;

        return pi * radius * radius;
    }

    public synchronized Progress getProgress() {
        if (progress == null) {
            synchronized (this) {
                if (progress == null) {
                    progress = new Progress(repeats);
                }
            }
        }

        return progress;
    }

}
