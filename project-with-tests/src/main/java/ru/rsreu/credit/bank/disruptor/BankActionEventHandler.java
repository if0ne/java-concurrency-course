package ru.rsreu.credit.bank.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import ru.rsreu.credit.actions.SyncBankAction;

import java.util.List;
import java.util.stream.Collectors;

public class BankActionEventHandler implements EventHandler<BankActionEvent> {
    private final List<Disruptor<BankActionEvent>> queues;
    private final List<RingBuffer<BankActionEvent>> ringBuffers;
    public BankActionEventHandler(List<Disruptor<BankActionEvent>> queues) {
        this.queues = queues;
        ringBuffers = queues.stream().map(Disruptor::getRingBuffer).collect(Collectors.toList());
    }

    @Override
    public void onEvent(BankActionEvent bankActionEvent, long sequence, boolean endOfBatch) throws Exception {
        int index = Math.abs(bankActionEvent.get().getClient().hashCode()) % queues.size();
        long sequenceId = ringBuffers.get(index).next();
        BankActionEvent event = ringBuffers.get(index).get(sequenceId);
        event.set(bankActionEvent.get());
        ringBuffers.get(index).publish(sequenceId);
    }
}
