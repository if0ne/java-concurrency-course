package ru.rsreu.credit.actions;

import ru.rsreu.credit.client.Client;
import ru.rsreu.credit.exceptions.BankActionException;

public interface BankAction {
    void perform() throws BankActionException;
    Client getClient();
}
