package ru.rsreu.credit.client;

import ru.rsreu.credit.bank.Account;
import ru.rsreu.credit.bank.Accounts;
import ru.rsreu.credit.bank.Currency;

import java.util.Map;
import java.util.UUID;

public class ClientInfo {
    private final UUID id;
    private final Accounts accounts;

    public ClientInfo(Client client, Accounts accounts) {
        this.id = client.id();
        this.accounts = accounts;
    }

    public UUID id() {
        return id;
    }

    public Accounts accounts() {
        return accounts;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "id=" + id +
                ", accounts=" + accounts +
                '}';
    }
}
