package ru.rsreu.credit.bank;

import ru.rsreu.credit.bank.storage.ClientDatabase;

public abstract class AbstractBank implements Bank {
    protected ClientDatabase database;
}
