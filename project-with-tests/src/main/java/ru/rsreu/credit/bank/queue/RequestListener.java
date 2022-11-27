package ru.rsreu.credit.bank.queue;

import ru.rsreu.credit.actions.BankAction;
import ru.rsreu.credit.actions.SyncBankAction;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class RequestListener implements Runnable {

    private final BlockingQueue<SyncBankAction> request;

    public RequestListener(BlockingQueue<SyncBankAction> request) {
        this.request = request;
    }

    @Override
    public void run() {
        try {
            listen();
        } catch (InterruptedException exception) {
            System.out.println("Queue listener is interrupt");
        } catch (Exception exception) {
            System.out.println("Got exception: " + exception.getMessage());
        }
    }

    private void listen() throws Exception {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            SyncBankAction action = request.take();
            action.perform();
            action.getSyncLatch().await(1_000_000, TimeUnit.MILLISECONDS);
        }
    }
}
