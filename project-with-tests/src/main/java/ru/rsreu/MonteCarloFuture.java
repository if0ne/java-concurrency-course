package ru.rsreu;

import java.util.concurrent.Callable;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloFuture implements Callable<Long>, Flow.Publisher<Long> {
    private final SubmissionPublisher<Long> publisher = new SubmissionPublisher<>();
    private final long repeats;
    private final int logFrequency;

    public MonteCarloFuture(long repeats, int logFrequency) {
        this.repeats = repeats;
        this.logFrequency = logFrequency;
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
                publisher.submit(step);
            }
        }

        return inCircle;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super Long> subscriber) {
        publisher.subscribe(subscriber);
    }
}
