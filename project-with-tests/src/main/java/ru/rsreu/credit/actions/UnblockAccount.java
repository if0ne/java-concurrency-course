package ru.rsreu.credit.actions;

import ru.rsreu.credit.bank.Accounts;
import ru.rsreu.credit.client.Client;
import ru.rsreu.credit.bank.ClientDatabase;
import ru.rsreu.credit.bank.Currency;

public class UnblockAccount implements BankAction {
    private final Client client;
    private final Currency currency;
    private final ClientDatabase database;

    public UnblockAccount(Client client, Currency currency, ClientDatabase database) {
        this.client = client;
        this.currency = currency;
        this.database = database;
    }

    @Override
    public void perform() {
        Accounts accounts = database.getClientAccounts(client);
        accounts.unblockAccount(currency);
    }
}
