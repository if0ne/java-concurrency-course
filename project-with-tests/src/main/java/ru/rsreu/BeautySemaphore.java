package ru.rsreu;

import java.util.concurrent.TimeUnit;

public class BeautySemaphore {
    private final int permits;
    private int currentPermits = 0;

    private final Object lock = new Object();

    public BeautySemaphore(int permits) {
        this.permits = permits;
    }

    public void acquire() throws InterruptedException {
        acquire(1);
    }

    public void acquire(int permits) throws InterruptedException {
        synchronized (lock) {
            while (currentPermits + permits > this.permits) {
                lock.wait();
            }
            currentPermits += permits;
        }
    }

    public boolean tryAcquire() {
        return tryAcquire(1);
    }

    public boolean tryAcquire(int permits) {
        synchronized (lock) {
            if (permits + currentPermits <= this.permits) {
                currentPermits += permits;
                return true;
            }
        }
        return false;
    }

    public void release() {
        release(1);
    }

    public void release(int permits) {
        if (permits < 0) {
            throw new IllegalArgumentException("Permits must be more than 0");
        }
        synchronized (lock) {
            currentPermits -= permits;
            currentPermits = Math.max(currentPermits, 0);
            lock.notifyAll();
        }
    }

}
