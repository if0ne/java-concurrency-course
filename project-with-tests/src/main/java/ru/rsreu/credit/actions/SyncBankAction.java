package ru.rsreu.credit.actions;

import ru.rsreu.credit.exceptions.BankActionException;

import java.util.concurrent.CountDownLatch;

public class SyncBankAction implements BankAction {
    private final CountDownLatch latch = new CountDownLatch(1);
    private final BankAction action;

    public SyncBankAction(BankAction action) {
        this.action = action;
    }

    public CountDownLatch getSyncLatch() {
        return latch;
    }

    @Override
    public void perform() throws BankActionException {
        action.perform();
        latch.countDown();
    }
}
