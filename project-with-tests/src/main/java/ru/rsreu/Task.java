package ru.rsreu;

import java.util.concurrent.Flow;

public class Task implements Flow.Subscriber<Double> {

    private final int id;
    private final HeavyCircleSquareCalculator task;
    private final Thread thread;
    private TaskStatus status;

    public Task(int id, HeavyCircleSquareCalculator task) {
        this.id = id;
        this.task = task;
        this.thread = new Thread(task);
        this.task.subscribe(this);
        status = TaskStatus.QUEUE;
    }

    public int getId() {
        return id;
    }

    public void start() {
        thread.start();
        status = TaskStatus.RUNNING;
    }

    public void stop() {
        if (status == TaskStatus.RUNNING) {
            thread.interrupt();
            status = TaskStatus.INTERRUPTED;
        }
    }

    public void join() {
        try {
            thread.join(100_000_000);
            status = TaskStatus.FINISHED;
        } catch (InterruptedException exception) {
            status = TaskStatus.INTERRUPTED;
        }
    }

    @Override
    public String toString() {
        return String.format("Task [id=%d, status=%s]", id, status);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(1);
    }

    @Override
    public void onNext(Double item) {
        status = TaskStatus.FINISHED;
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
