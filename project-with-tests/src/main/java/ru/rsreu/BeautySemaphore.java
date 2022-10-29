package ru.rsreu;

public class BeautySemaphore {
    private int permits;

    public BeautySemaphore(int permits) {
        this.permits = permits;
    }

    public void acquire() throws InterruptedException {
        while (permits <= 0) {
            wait();
        }

        permits--;
    }

    public synchronized void release() {
        permits++;
        notify();
    }

}
