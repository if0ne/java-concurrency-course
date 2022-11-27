package ru.rsreu.credit.bank;

import ru.rsreu.credit.exceptions.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Accounts {
    private final Map<Currency, Account> accounts;

    public Set<Map.Entry<Currency, Account>> accounts() {
        return accounts.entrySet();
    }

    private Accounts(Map<Currency, Account> accounts) {
        this.accounts = accounts;
    }

    public static Accounts createConcurrentAccounts() {
        Map<Currency, Account> accounts = new ConcurrentHashMap<>();
        for (Currency currency : Currency.values()) {
            accounts.put(currency, new Account(BigDecimal.ZERO));
        }

        return new Accounts(accounts);
    }

    public static Accounts createAccounts() {
        Map<Currency, Account> accounts = new HashMap<>();
        for (Currency currency : Currency.values()) {
            accounts.put(currency, new Account(BigDecimal.ZERO));
        }

        return new Accounts(accounts);
    }

    public void deposit(Currency currency, BigDecimal value) throws BankActionException {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalMoneyValue("Deposit value must be more than 0");
        }

        accounts.computeIfPresent(currency, (key, account) -> {
            this.unblockAccount(currency);
            account.setValue(account.value().add(value));

            return account;
        });
    }

    public void withdraw(Currency currency, BigDecimal value) throws BankActionException {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalMoneyValue("Withdraw value must be more than 0");
        }

        if (accounts.get(currency).value().compareTo(value) < 0) {
            throw new NotEnoughMoneyException("Not enough money to withdraw");
        }

        accounts.computeIfPresent(currency, (key, account) -> {
            account.setStatus(Status.Unblocked);
            account.setValue(account.value().subtract(value));

            return account;
        });
    }

    public void blockAccount(Currency currency, BigDecimal value) throws BankActionException {
        if (accounts.get(currency).status() == Status.Blocked) {
            throw new AlreadyBlockedAccountException("Currency " + currency + " already blocked");
        }
        if (accounts.get(currency).value().compareTo(value) < 0) {
            throw new NotEnoughMoneyException("Not enough money to block account");
        }

        accounts.computeIfPresent(currency, (key, account) -> {
            account.setStatus(Status.Blocked);
            return account;
        });
    }

    public void unblockAccount(Currency currency) {
        accounts.computeIfPresent(currency, (key, account) -> {
            account.setStatus(Status.Unblocked);
            return account;
        });
    }

    public Account getAccount(Currency currency) {
        return accounts.get(currency);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Accounts{"); builder.append("accounts=[");
        for (Map.Entry<Currency, Account> account: accounts()) {
            builder.append("{currency="); builder.append(account.getKey()); builder.append(", ");
            builder.append("{value="); builder.append(account.getValue()); builder.append("}, ");
        }
        builder.append("]}");
        return builder.toString();
    }
}
