package ru.rsreu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelCircleSquareCalculator implements Flow.Subscriber<Long> {

    private final double radius;
    private final long repeats;
    private final int poolSize;
    private final ExecutorService executorService;

    private long progress;

    public ParallelCircleSquareCalculator(double radius, long repeats, int poolSize) {
        this.radius = radius;
        this.repeats = repeats;
        this.poolSize = poolSize;
        this.progress = 0;
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    public double calculateSquare() {
        long repeatsPerTask = repeats / poolSize;

        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < poolSize; ++i) {
            MonteCarloFuture task = new MonteCarloFuture(repeatsPerTask, 4);
            task.subscribe(this);
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

    private synchronized void updateProgress(long delta) {
        progress += delta;
        int percent = (int) Math.floor((double) progress / repeats * 100.0);
        System.out.printf("Progress %d%%\n", percent);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Long item) {
        updateProgress(item);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
