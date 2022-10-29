package ru.rsreu;

import java.util.concurrent.*;

public class MonteCarloFuture implements Callable<Long> {
    private final long repeats;
    private final int logFrequency;
    private final ParallelCircleSquareCalculator calculator;
    private final Semaphore semaphore;
    private final CountDownLatch latch;

    public MonteCarloFuture(long repeats, int logFrequency, CountDownLatch latch, Semaphore semaphore, ParallelCircleSquareCalculator calculator) {
        this.repeats = repeats;
        this.logFrequency = logFrequency;
        this.latch = latch;
        this.calculator = calculator;
        this.semaphore = semaphore;
    }

    @Override
    public Long call() throws Exception {
        semaphore.acquire();

        long inCircle = 0;
        long step = repeats / (logFrequency + 1);
        long currentLog = 0;

        for (long i = 0; i < repeats; ++i) {
            double x = ThreadLocalRandom.current().nextDouble();
            double y = ThreadLocalRandom.current().nextDouble();

            if (x*x + y*y < 1.0) {
                inCircle++;
            }

            if (i / step > currentLog) {
                currentLog++;
                calculator.getProgress().updateProgress(step);
            }
        }
        semaphore.release();
        latch.countDown();
        logResult(System.currentTimeMillis());

        return inCircle;
    }

    private void logResult(long startTime) throws InterruptedException {
        if (latch.await(Long.MAX_VALUE / 2, TimeUnit.MILLISECONDS)) {
            System.out.printf("Time from last task [%s]: %d ms\n", Thread.currentThread().getName(), System.currentTimeMillis() - startTime);
        }
    }

}
