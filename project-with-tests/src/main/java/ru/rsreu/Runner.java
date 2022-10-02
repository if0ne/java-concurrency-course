package ru.rsreu;

import java.util.Scanner;

public class Runner {

    private static boolean isExit = false;
    private static final TaskExecutor taskExecutor = new TaskExecutor();

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        while (!isExit) {
            printMenu();

            String[] command = reader.nextLine().split(" ");

            if (command.length == 0) {
                continue;
            }

            try {
                handleCommand(command);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("Enter command:");
        System.out.println("1. start <n> - start task with time parameter <n>");
        System.out.println("2. stop <n> - stop task with id <n>");
        System.out.println("3. await <n> - block console until task with id <n> is finished");
        System.out.println("4. info - info about all tasks");
        System.out.println("5. exit");
    }

    private static void handleCommand(String[] args) throws IllegalArgumentException {
        switch (args[0]) {
            case "start": {
                if (args.length != 2) {
                    throw new IllegalArgumentException("Wrong command format");
                }
                long n = Long.parseLong(args[1]);
                int id = taskExecutor.createTask(new HeavyCircleSquareCalculator(n, 64.0));
                System.out.printf("Created task with id %d\n", id);
                break;
            }
            case "stop": {
                if (args.length != 2) {
                    throw new IllegalArgumentException("Wrong command format");
                }
                int n = Integer.parseInt(args[1]);
                System.out.printf("Stopping task with id %d\n", n);
                taskExecutor.stopTask(n);
                break;
            }
            case "await": {
                if (args.length != 2) {
                    throw new IllegalArgumentException("Wrong command format");
                }
                int n = Integer.parseInt(args[1]);
                System.out.printf("Waiting task with id %d\n", n);
                taskExecutor.awaitTask(n);
                System.out.printf("Task with id %d finished\n", n);
                break;
            }
            case "info": {
                String info = taskExecutor.getAllInfo();
                System.out.println(info);
                break;
            }
            case "exit": {
                isExit = true;
                System.out.println("Exit...");
                break;
            }
            default: {
                System.out.println("Unknown command");
            }
        }
    }

}
