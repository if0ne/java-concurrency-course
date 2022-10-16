package ru.rsreu;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloFuture implements Callable<Long> {
    private final long repeats;
    private final int logFrequency;
    private final ParallelCircleSquareCalculator calculator;

    public MonteCarloFuture(long repeats, int logFrequency, ParallelCircleSquareCalculator calculator) {
        this.repeats = repeats;
        this.logFrequency = logFrequency;
        this.calculator = calculator;
    }

    @Override
    public Long call() {
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

        return inCircle;
    }
}
