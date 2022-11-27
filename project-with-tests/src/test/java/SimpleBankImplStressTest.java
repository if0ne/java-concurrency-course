import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import ru.rsreu.credit.bank.*;
import ru.rsreu.credit.client.Client;
import ru.rsreu.credit.client.ClientInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SimpleBankImplStressTest {
    private final Bank bank = new SimpleBankImpl();

    @Test
    public void stressTest() throws Exception {
        final int clientCount = 1000;
        final List<Client> clients = new ArrayList<>();

        for (int i = 0; i < clientCount; ++i) {
            clients.add(bank.createClient());
        }

        List<Thread> threads = new ArrayList<>();
        CountDownLatch prepareLatch = new CountDownLatch(clientCount);

        for (Client client : clients) {
            threads.add(new Thread(() -> {
                try {
                    prepareLatch.countDown();
                    prepareLatch.await();
                    bank.deposit(client, Currency.RUB, new BigDecimal(10000));
                    for (int i = 0; i < 10; i++) {
                        bank.blockAccount(client, Currency.RUB, new BigDecimal(1000));
                        bank.unblockAccount(client, Currency.RUB);
                        bank.withdraw(client, Currency.RUB, new BigDecimal(1000));
                    }
                    bank.deposit(client, Currency.USD, new BigDecimal(1));
                } catch (Exception exception) {
                    Assertions.fail();
                }
            }));
        }

        long start = System.nanoTime();
        for (Thread thread : threads) {
            thread.start();
        }


        for (Thread thread : threads) {
            thread.join();
        }
        System.out.printf("%.2f requests per second\n", (clientCount) / ((System.nanoTime() - start) / 1000000000.0));

        for (Client client : clients) {
            ClientInfo info = bank.getClientInfo(client);
            Account rubAccount = info.accounts().getAccount(Currency.RUB);
            Account usdAccount = info.accounts().getAccount(Currency.USD);

            Assertions.assertEquals(Status.Unblocked, rubAccount.status());
            Assertions.assertEquals(BigDecimal.ZERO, rubAccount.value());
            Assertions.assertEquals(BigDecimal.ONE, usdAccount.value());
        }
    }
}
