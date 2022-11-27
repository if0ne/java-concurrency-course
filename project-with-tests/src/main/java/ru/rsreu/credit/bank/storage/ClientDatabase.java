package ru.rsreu.credit.bank.storage;

import ru.rsreu.credit.bank.Accounts;
import ru.rsreu.credit.client.Client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ClientDatabase {
    protected final Map<Client, Accounts> clients;

    public ClientDatabase() {
        this.clients = new ConcurrentHashMap<>();
    }

    public abstract Client createClient();

    public Accounts getClientAccounts(Client client) {
        return clients.get(client);
    }
}
