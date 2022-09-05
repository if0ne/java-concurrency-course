package ru.rsreu;

public final class SimpleCircleSquareCalculator implements CircleSquareCalculator {
    @Override
    public double calculate(double radius) {
        if (radius < 0.0) {
            throw new IllegalArgumentException("Circle's radius must be bigger than 0");
        }

        return Math.PI * radius * radius;
    }
}
