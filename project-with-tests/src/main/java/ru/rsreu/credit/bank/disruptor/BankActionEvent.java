package ru.rsreu.credit.bank.disruptor;

import ru.rsreu.credit.actions.SyncBankAction;

public class BankActionEvent {
    private SyncBankAction action;

    public SyncBankAction get() {
        return action;
    }

    public void set(SyncBankAction action) {
        this.action = action;
    }
}
