package ru.rsreu;

import java.util.Scanner;

public class Runner {

    public static void main(String[] args) {
        long init = System.currentTimeMillis();
        ParallelCircleSquareCalculator calculator = new ParallelCircleSquareCalculator(1.0, 500_000_000, 8);
        double square = calculator.calculateSquare();
        long elapsed = System.currentTimeMillis() - init;
        System.out.printf("Circle square is %f\n", square);
        System.out.printf("Elapsed time: %d ms\n", elapsed);
    }

}
