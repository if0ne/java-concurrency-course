package ru.rsreu.credit.bank.queue;

import ru.rsreu.credit.actions.*;
import ru.rsreu.credit.bank.AbstractBank;
import ru.rsreu.credit.bank.Currency;
import ru.rsreu.credit.bank.storage.SingleThreadClientDatabase;
import ru.rsreu.credit.client.Client;
import ru.rsreu.credit.client.ClientInfo;
import ru.rsreu.credit.exceptions.BankActionException;

import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueBankImpl extends AbstractBank {

    private final BlockingQueue<SyncBankAction> requests = new LinkedBlockingQueue<>();
    private final Thread requestListener;

    public QueueBankImpl() {
        super();

        database = new SingleThreadClientDatabase();
        requestListener = new Thread(new RequestListener(requests));
        requestListener.start();
    }

    @Override
    public Client createClient() {
        return this.database.createClient();
    }

    @Override
    public void deposit(Client client, Currency currency, BigDecimal value) throws BankActionException {
        Deposit deposit = new Deposit(client, currency, value, database);
        requests.add(new SyncBankAction(deposit));
    }

    @Override
    public void withdraw(Client client, Currency currency, BigDecimal value) throws BankActionException  {
        Withdraw withdraw = new Withdraw(client, currency, value, database);
        requests.add(new SyncBankAction(withdraw));
    }

    @Override
    public void blockAccount(Client client, Currency currency, BigDecimal value) throws BankActionException {
        BlockAccount blockAccount = new BlockAccount(client, currency, value, database);
        requests.add(new SyncBankAction(blockAccount));
    }

    @Override
    public void unblockAccount(Client client, Currency currency) {
        UnblockAccount unblockAccount = new UnblockAccount(client, currency, database);
        requests.add(new SyncBankAction(unblockAccount));
    }

    @Override
    public ClientInfo getClientInfo(Client client) {
        return new ClientInfo(client, database.getClientAccounts(client));
    }
}
