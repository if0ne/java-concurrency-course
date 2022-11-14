package ru.rsreu.credit.bank;

public abstract class AbstractBank implements Bank {
    protected ClientDatabase database;

    public AbstractBank() {
        database = new ClientDatabase();
    }
}
