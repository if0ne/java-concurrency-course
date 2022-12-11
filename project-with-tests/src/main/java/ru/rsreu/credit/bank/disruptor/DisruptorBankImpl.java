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

public class DisruptorBankImpl extends AbstractBank {

    private final int BUFFER_SIZE = 65536;
    private final Disruptor<BankActionEvent> disruptor;
    private final RingBuffer<BankActionEvent> ringBuffer;

    public DisruptorBankImpl() {
        super();
        database = new SingleThreadClientDatabase();

        WaitStrategy waitStrategy = new BlockingWaitStrategy();
        disruptor = new Disruptor<>(new BankActionEventFactory(), BUFFER_SIZE, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, waitStrategy);
        ringBuffer = disruptor.getRingBuffer();
        disruptor.handleEventsWith(new BankActionEventHandler());

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
}
