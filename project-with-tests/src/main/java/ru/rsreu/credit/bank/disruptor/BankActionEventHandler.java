package ru.rsreu.credit.bank.disruptor;

import com.lmax.disruptor.EventHandler;

import java.util.concurrent.TimeUnit;

public class BankActionEventHandler implements EventHandler<BankActionEvent> {
    @Override
    public void onEvent(BankActionEvent bankActionEvent, long sequence, boolean endOfBatch) throws Exception {
        bankActionEvent.get().perform();
        bankActionEvent.get().getSyncLatch().await(1_000_000, TimeUnit.MILLISECONDS);
    }
}
