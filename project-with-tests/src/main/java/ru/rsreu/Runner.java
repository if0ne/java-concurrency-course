package ru.rsreu;

public class Runner {

    public static void main(String[] args) throws InterruptedException {
        HeavyCircleSquareCalculator calculator = new HeavyCircleSquareCalculator(
                100_000_000,
                64.0,
                20
        );
        Thread calculationThread = new Thread(calculator, "Circle Square Calculator Thread");
        long startTime = System.currentTimeMillis();
        calculationThread.start();
        calculationThread.join();
        long endTime = System.currentTimeMillis();
        System.out.printf("Calculator result: %f\n", calculator.getSquare());
        System.out.printf("Calculation time: %dms", endTime - startTime);
    }

}
