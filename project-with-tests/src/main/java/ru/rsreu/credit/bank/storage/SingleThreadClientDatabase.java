package ru.rsreu.credit.bank.storage;

import ru.rsreu.credit.bank.Accounts;
import ru.rsreu.credit.client.Client;

public class SingleThreadClientDatabase extends ClientDatabase {
    @Override
    public Client createClient() {
        Accounts accounts = Accounts.createAccounts();
        Client client = new Client();
        clients.put(client, accounts);

        return client;
    }
}
