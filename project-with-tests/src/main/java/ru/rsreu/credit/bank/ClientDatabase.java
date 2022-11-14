package ru.rsreu.credit.bank;

import ru.rsreu.credit.bank.Accounts;
import ru.rsreu.credit.client.Client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientDatabase {
    private final ConcurrentMap<Client, Accounts> clients;

    public ClientDatabase() {
        this.clients = new ConcurrentHashMap<>();
    }

    public Client createClient() {
        Accounts accounts = new Accounts();
        Client client = new Client();
        clients.put(client, accounts);

        return client;
    }

    public Accounts getClientAccounts(Client client) {
        return clients.get(client);
    }
}
