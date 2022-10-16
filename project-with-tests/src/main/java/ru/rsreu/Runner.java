package ru.rsreu;

public class Runner {

    public static void main(String[] args) throws InterruptedException {
        ResultContainer container = new ResultContainer(500, 'a');
        SearchLetterTask firstFileReader = new SearchLetterTask(args[0], container);
        SearchLetterTask secondFileReader = new SearchLetterTask(args[1], container);

        Thread firstThread = new Thread(firstFileReader);
        Thread secondThread = new Thread(secondFileReader);
        firstThread.start();
        secondThread.start();

        firstThread.join();
        secondThread.join();

        System.out.printf("Total number: %d\n", container.getNumberLetter());
    }

}
