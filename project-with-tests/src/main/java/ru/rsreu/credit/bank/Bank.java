package ru.rsreu.credit.bank;

import ru.rsreu.credit.client.Client;
import ru.rsreu.credit.client.ClientInfo;
import ru.rsreu.credit.exceptions.BankActionException;

import java.math.BigDecimal;

public interface Bank {
    Client createClient();
    void deposit(Client client, Currency currency, BigDecimal value) throws BankActionException;
    void withdraw(Client client, Currency currency, BigDecimal value) throws BankActionException;
    void blockAccount(Client client, Currency currency, BigDecimal value) throws BankActionException;
    void unblockAccount(Client client, Currency currency);
    ClientInfo getClientInfo(Client client);
}
