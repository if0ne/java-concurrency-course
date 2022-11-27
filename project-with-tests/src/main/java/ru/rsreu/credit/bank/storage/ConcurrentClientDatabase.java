package ru.rsreu.credit.bank.storage;

import ru.rsreu.credit.bank.Accounts;
import ru.rsreu.credit.client.Client;

public class ConcurrentClientDatabase extends ClientDatabase {
    public ConcurrentClientDatabase() {
        super();
    }

    @Override
    public Client createClient() {
        Accounts accounts = Accounts.createConcurrentAccounts();
        Client client = new Client();
        clients.put(client, accounts);

        return client;
    }
}
