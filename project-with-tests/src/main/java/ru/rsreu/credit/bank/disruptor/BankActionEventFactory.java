package ru.rsreu.credit.bank.disruptor;

import com.lmax.disruptor.EventFactory;

public class BankActionEventFactory implements EventFactory<BankActionEvent> {
    @Override
    public BankActionEvent newInstance() {
        return new BankActionEvent();
    }
}
