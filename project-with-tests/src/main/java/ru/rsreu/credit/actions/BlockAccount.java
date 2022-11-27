package ru.rsreu.credit.actions;

import ru.rsreu.credit.bank.Accounts;
import ru.rsreu.credit.bank.storage.ClientDatabase;
import ru.rsreu.credit.client.Client;
import ru.rsreu.credit.bank.Currency;
import ru.rsreu.credit.exceptions.BankActionException;

import java.math.BigDecimal;

public class BlockAccount implements BankAction {

    private final Client client;
    private final Currency currency;
    private final BigDecimal value;
    private final ClientDatabase database;

    public BlockAccount(Client client, Currency currency, BigDecimal value, ClientDatabase database) {
        this.client = client;
        this.currency = currency;
        this.value = value;
        this.database = database;
    }

    @Override
    public void perform() throws BankActionException {
        Accounts accounts = database.getClientAccounts(client);
        accounts.blockAccount(currency, value);
    }
}
