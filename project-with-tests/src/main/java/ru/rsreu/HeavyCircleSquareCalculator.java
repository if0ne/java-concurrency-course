package ru.rsreu;

public final class HeavyCircleSquareCalculator implements Runnable {
    private final long monteCarloRepeats;
    private final double radius;
    private final long logFrequency;

    private double square;

    public HeavyCircleSquareCalculator(long monteCarloRepeats, double radius, long logFrequency) {
        this.monteCarloRepeats = monteCarloRepeats;
        this.radius = radius;
        this.logFrequency = logFrequency;
        this.square = 0.0;
    }

    private double monteCarloPi() {
        int in_circle = 0;
        long step = monteCarloRepeats / logFrequency;
        for (long i = 0; i < monteCarloRepeats; ++i) {
            double x = Math.random();
            double y = Math.random();

            if (x*x + y*y < 1.0) {
                in_circle++;
            }

            if (i % step == 0) {
                int percent = (int) Math.floor((double) i / monteCarloRepeats * 100.0);
                System.out.printf("Current progress: %d%%\n", percent);
            }
        }

        return 4.0 * in_circle / monteCarloRepeats;
    }

    public double getSquare() {
        return square;
    }

    @Override
    public void run() {
        square = monteCarloPi() * radius * radius;
    }
}
