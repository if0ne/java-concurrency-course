package ru.rsreu.credit.bank;

import ru.rsreu.credit.actions.BlockAccount;
import ru.rsreu.credit.actions.Deposit;
import ru.rsreu.credit.actions.UnblockAccount;
import ru.rsreu.credit.actions.Withdraw;
import ru.rsreu.credit.client.Client;
import ru.rsreu.credit.client.ClientInfo;
import ru.rsreu.credit.exceptions.BankActionException;

import java.math.BigDecimal;

public class SimpleBankImpl extends AbstractBank {

    public SimpleBankImpl() {
        super();
    }

    @Override
    public Client createClient() {
        return this.database.createClient();
    }

    @Override
    public void deposit(Client client, Currency currency, BigDecimal value) throws BankActionException {
        Deposit deposit = new Deposit(client, currency, value, database);
        deposit.perform();
    }

    @Override
    public void withdraw(Client client, Currency currency, BigDecimal value) throws BankActionException  {
        Withdraw withdraw = new Withdraw(client, currency, value, database);
        withdraw.perform();
    }

    @Override
    public void blockAccount(Client client, Currency currency, BigDecimal value) throws BankActionException {
        BlockAccount blockAccount = new BlockAccount(client, currency, value, database);
        blockAccount.perform();
    }

    @Override
    public void unblockAccount(Client client, Currency currency) {
        UnblockAccount unblockAccount = new UnblockAccount(client, currency, database);
        unblockAccount.perform();
    }

    @Override
    public ClientInfo getClientInfo(Client client) {
        return new ClientInfo(client, database.getClientAccounts(client));
    }
}
