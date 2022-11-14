import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rsreu.credit.bank.*;
import ru.rsreu.credit.client.Client;
import ru.rsreu.credit.exceptions.AlreadyBlockedAccountException;
import ru.rsreu.credit.exceptions.BankActionException;
import ru.rsreu.credit.exceptions.IllegalMoneyValue;
import ru.rsreu.credit.exceptions.NotEnoughMoneyException;

import java.math.BigDecimal;

public class BankApiTest {
    private final Bank bank = new SimpleBankImpl();

    @Test
    public void createClientTest() {
        Client client = bank.createClient();
        Assertions.assertNotEquals(null, client);
    }

    @Test
    public void createTwoClientsTest() {
        final Client firstClient = bank.createClient();
        final Client secondClient = bank.createClient();

        Assertions.assertNotSame(firstClient, secondClient);
    }

    @Test
    public void depositTest() {
        final Client client = bank.createClient();
        final Currency currency = Currency.RUB;
        final BigDecimal sum = new BigDecimal(10000);
        final BigDecimal expectedSum = new BigDecimal(10000);

        try {
            bank.deposit(client, currency, sum);
            Account account = bank.getClientInfo(client).accounts().getAccount(currency);
            Assertions.assertEquals(Status.Unblocked, account.status());
            Assertions.assertEquals(expectedSum, account.value());
        } catch (BankActionException e) {
            Assertions.fail();
        }
    }

    @Test
    public void depositWrongArgumentTest() {
        final Client client = bank.createClient();
        final Currency currency = Currency.RUB;
        final BigDecimal sum = new BigDecimal(-10000);

        Assertions.assertThrows(IllegalMoneyValue.class, () -> {
            bank.deposit(client, currency, sum);
        });
    }

    @Test
    public void withdrawTest() {
        final Client client = bank.createClient();
        final Currency currency = Currency.RUB;
        final BigDecimal depositSum = new BigDecimal(10000);
        final BigDecimal withdrawSum = new BigDecimal(7620);
        final BigDecimal expectedSum = new BigDecimal(2380);

        try {
            bank.deposit(client, currency, depositSum);
            bank.withdraw(client, currency, withdrawSum);
            Account account = bank.getClientInfo(client).accounts().getAccount(currency);
            Assertions.assertEquals(Status.Unblocked, account.status());
            Assertions.assertEquals(expectedSum, account.value());
        } catch (BankActionException e) {
            Assertions.fail();
        }
    }

    @Test
    public void withdrawWrongArgumentTest() {
        final Client client = bank.createClient();
        final Currency currency = Currency.RUB;
        final BigDecimal sum = new BigDecimal(-10000);

        Assertions.assertThrows(IllegalMoneyValue.class, () -> {
            bank.deposit(client, currency, new BigDecimal(10000));
            bank.withdraw(client, currency, sum);
        });
    }

    @Test
    public void withdrawNotEnoughMoneyTest() {
        final Client client = bank.createClient();
        final Currency currency = Currency.RUB;
        final BigDecimal depositSum = new BigDecimal(10000);
        final BigDecimal withdrawSum = new BigDecimal(20000);

        Assertions.assertThrows(NotEnoughMoneyException.class, () -> {
            bank.deposit(client, currency, depositSum);
            bank.withdraw(client, currency, withdrawSum);
        });
    }

    @Test
    public void blockAccountTest() {
        final Client client = bank.createClient();
        final Currency currency = Currency.RUB;
        final BigDecimal depositSum = new BigDecimal(10000);
        final BigDecimal blockSum = new BigDecimal(5000);
        final BigDecimal expectedSum = new BigDecimal(10000);

        try {
            bank.deposit(client, currency, depositSum);
            bank.blockAccount(client, currency, blockSum);
            Account account = bank.getClientInfo(client).accounts().getAccount(currency);
            Assertions.assertEquals(Status.Blocked, account.status());
            Assertions.assertEquals(expectedSum, account.value());
        } catch (BankActionException e) {
            Assertions.fail();
        }
    }

    @Test
    public void twiceBlockAccountTest() {
        final Client client = bank.createClient();
        final Currency currency = Currency.RUB;
        final BigDecimal depositSum = new BigDecimal(10000);
        final BigDecimal blockSum = new BigDecimal(5000);

        Assertions.assertThrows(AlreadyBlockedAccountException.class, () -> {
            bank.deposit(client, currency, depositSum);
            bank.blockAccount(client, currency, blockSum);
            bank.blockAccount(client, currency, blockSum);
        });
    }

    @Test
    public void blockNotEnoughMoneyAccountTest() {
        final Client client = bank.createClient();
        final Currency currency = Currency.RUB;
        final BigDecimal depositSum = new BigDecimal(10000);
        final BigDecimal blockSum = new BigDecimal(15000);

        Assertions.assertThrows(NotEnoughMoneyException.class, () -> {
            bank.deposit(client, currency, depositSum);
            bank.blockAccount(client, currency, blockSum);
        });
    }

    @Test
    public void unblockAfterDepositAccountTest() {
        final Client client = bank.createClient();
        final Currency currency = Currency.RUB;
        final BigDecimal depositSum = new BigDecimal(10000);
        final BigDecimal blockSum = new BigDecimal(5000);
        final BigDecimal expectedSum = new BigDecimal(20000);

        try {
            bank.deposit(client, currency, depositSum);
            bank.blockAccount(client, currency, blockSum);
            bank.deposit(client, currency, depositSum);
            Account account = bank.getClientInfo(client).accounts().getAccount(currency);
            Assertions.assertEquals(Status.Unblocked, account.status());
            Assertions.assertEquals(expectedSum, account.value());
        } catch (BankActionException e) {
            Assertions.fail();
        }
    }

    @Test
    public void blockAndDepositCurrenciesTest() {
        final Client client = bank.createClient();
        final Currency rubCurrency = Currency.RUB;
        final Currency usdCurrency = Currency.USD;
        final BigDecimal depositSum = new BigDecimal(10000);
        final BigDecimal blockSum = new BigDecimal(5000);
        final BigDecimal expectedSum = new BigDecimal(10000);

        try {
            bank.deposit(client, rubCurrency, depositSum);
            bank.blockAccount(client, rubCurrency, blockSum);
            bank.deposit(client, usdCurrency, depositSum);

            Account rubAccount = bank.getClientInfo(client).accounts().getAccount(rubCurrency);
            Assertions.assertEquals(Status.Blocked, rubAccount.status());
            Assertions.assertEquals(expectedSum, rubAccount.value());

            Account usdAccount = bank.getClientInfo(client).accounts().getAccount(usdCurrency);
            Assertions.assertEquals(Status.Unblocked, usdAccount.status());
            Assertions.assertEquals(expectedSum, usdAccount.value());
        } catch (BankActionException e) {
            Assertions.fail();
        }
    }

    @Test
    public void unblockDepositAccountTest() {
        final Client client = bank.createClient();
        final Currency currency = Currency.RUB;
        final BigDecimal depositSum = new BigDecimal(10000);
        final BigDecimal blockSum = new BigDecimal(5000);
        final BigDecimal expectedSum = new BigDecimal(10000);

        try {
            bank.deposit(client, currency, depositSum);
            bank.blockAccount(client, currency, blockSum);
            bank.unblockAccount(client, currency);

            Account account = bank.getClientInfo(client).accounts().getAccount(currency);
            Assertions.assertEquals(Status.Unblocked, account.status());
            Assertions.assertEquals(expectedSum, account.value());
        } catch (BankActionException e) {
            Assertions.fail();
        }
    }
}
