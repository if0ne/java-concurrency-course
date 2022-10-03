package ru.rsreu;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.ThreadLocalRandom;

public final class HeavyCircleSquareCalculator implements Runnable, Flow.Publisher<Double> {
    private final long monteCarloRepeats;
    private final double radius;
    private final SubmissionPublisher<Double> publisher;
    private double square;

    public HeavyCircleSquareCalculator(long monteCarloRepeats, double radius) {
        this.monteCarloRepeats = monteCarloRepeats;
        this.radius = radius;
        this.square = 0.0;
        this.publisher = new SubmissionPublisher<>();
    }

    private double monteCarloPi() throws InterruptedException {
        int in_circle = 0;
        for (long i = 0; i < monteCarloRepeats; ++i) {
            double x = ThreadLocalRandom.current().nextDouble();
            double y = ThreadLocalRandom.current().nextDouble();

            if (x*x + y*y < 1.0) {
                in_circle++;
            }

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
        }
        return 4.0 * in_circle / monteCarloRepeats;
    }

    public double getSquare() {
        return square;
    }

    @Override
    public void run() {
        try {
            square = monteCarloPi() * radius * radius;
            publisher.submit(square);
        } catch (InterruptedException exception) {
            System.out.println("Task stopped");
            square = 0.0;
        }
    }

    @Override
    public void subscribe(Flow.Subscriber<? super Double> subscriber) {
        publisher.subscribe(subscriber);
    }
}
