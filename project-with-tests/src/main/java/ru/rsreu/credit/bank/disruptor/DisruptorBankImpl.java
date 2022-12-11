package ru.rsreu.credit.bank.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import ru.rsreu.credit.actions.*;
import ru.rsreu.credit.bank.AbstractBank;
import ru.rsreu.credit.bank.Currency;
import ru.rsreu.credit.bank.storage.SingleThreadClientDatabase;
import ru.rsreu.credit.client.Client;
import ru.rsreu.credit.client.ClientInfo;
import ru.rsreu.credit.exceptions.BankActionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DisruptorBankImpl extends AbstractBank {

    private final int BUFFER_SIZE = 1024 * 32;
    private final int HANDLERS = 8;
    private final int HANDLER_SIZE = 1024 * 16;
    private final Disruptor<BankActionEvent> disruptor;
    private final RingBuffer<BankActionEvent> ringBuffer;

    private final List<Disruptor<BankActionEvent>> handlers;

    public DisruptorBankImpl() {
        super();
        database = new SingleThreadClientDatabase();

        disruptor = new Disruptor<>(new BankActionEventFactory(), BUFFER_SIZE, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new BlockingWaitStrategy());
        ringBuffer = disruptor.getRingBuffer();

        handlers = new ArrayList<>();
        for (int i = 0; i < HANDLERS; ++i) {
            Disruptor<BankActionEvent> handler = new Disruptor<>(new BankActionEventFactory(), HANDLER_SIZE, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BlockingWaitStrategy());
            handler.handleEventsWith((events, sequence, isEnd) -> {
               events.get().perform();
               events.get().getSyncLatch().await(1_000_000, TimeUnit.MILLISECONDS);
            });
            handler.start();
            handlers.add(handler);
        }

        disruptor.handleEventsWith(new BankActionEventHandler(handlers));

        disruptor.start();
    }

    @Override
    public Client createClient() {
        return this.database.createClient();
    }

    @Override
    public void deposit(Client client, Currency currency, BigDecimal value) throws BankActionException {
        Deposit deposit = new Deposit(client, currency, value, database);
        //ringBuffer.publishEvent((event, sequence, buffer) -> event.set(new SyncBankAction(deposit)));
        long sequenceId = ringBuffer.next();
        BankActionEvent event = ringBuffer.get(sequenceId);
        event.set(new SyncBankAction(deposit));
        ringBuffer.publish(sequenceId);
    }

    @Override
    public void withdraw(Client client, Currency currency, BigDecimal value) throws BankActionException {
        Withdraw withdraw = new Withdraw(client, currency, value, database);
        //ringBuffer.publishEvent((event, sequence, buffer) -> event.set(new SyncBankAction(withdraw)));
        long sequenceId = ringBuffer.next();
        BankActionEvent event = ringBuffer.get(sequenceId);
        event.set(new SyncBankAction(withdraw));
        ringBuffer.publish(sequenceId);
    }

    @Override
    public void blockAccount(Client client, Currency currency, BigDecimal value) throws BankActionException {
        BlockAccount blockAccount = new BlockAccount(client, currency, value, database);
        //ringBuffer.publishEvent((event, sequence, buffer) -> event.set(new SyncBankAction(blockAccount)));
        long sequenceId = ringBuffer.next();
        BankActionEvent event = ringBuffer.get(sequenceId);
        event.set(new SyncBankAction(blockAccount));
        ringBuffer.publish(sequenceId);
    }

    @Override
    public void unblockAccount(Client client, Currency currency) {
        UnblockAccount unblockAccount = new UnblockAccount(client, currency, database);
        //ringBuffer.publishEvent((event, sequence, buffer) -> event.set(new SyncBankAction(unblockAccount)));
        long sequenceId = ringBuffer.next();
        BankActionEvent event = ringBuffer.get(sequenceId);
        event.set(new SyncBankAction(unblockAccount));
        ringBuffer.publish(sequenceId);
    }

    @Override
    public ClientInfo getClientInfo(Client client) {
        return new ClientInfo(client, database.getClientAccounts(client));
    }

    @Override
    public void stopProcess() {
        disruptor.shutdown();
        for (int i = 0; i < HANDLERS; ++i) {
            handlers.get(i).shutdown();
        }
    }
}
