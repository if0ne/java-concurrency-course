package ru.rsreu;

public final class HeavyCircleSquareCalculator implements CircleSquareCalculator {
    private final long monteCarloRepeats;

    public HeavyCircleSquareCalculator(long monteCarloRepeats) {
        this.monteCarloRepeats = monteCarloRepeats;
    }

    private double monteCarloPi() {
        int in_circle = 0;
        for (long i = 0; i < monteCarloRepeats; ++i) {
            double x = Math.random();
            double y = Math.random();

            if (x*x + y*y < 1.0) {
                in_circle++;
            }
        }

        return 4.0 * in_circle / monteCarloRepeats;
    }

    @Override
    public double calculate(double radius) {
        if (radius < 0.0) {
            throw new IllegalArgumentException("Circle's radius must be bigger than 0");
        }

        return monteCarloPi() * radius * radius;
    }
}
