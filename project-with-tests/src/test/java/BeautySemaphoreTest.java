import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import ru.rsreu.BeautySemaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class BeautySemaphoreTest {

    private volatile int sharedInt = 0;

    @RepeatedTest(1000)
    public void singleAcquirePermitTest() throws InterruptedException {
        final int expectedAfterFirstThread = 1;
        final int expectedAfterSecondThread = 2;

        final BeautySemaphore semaphore = new BeautySemaphore(1);

        final CountDownLatch firstLatch = new CountDownLatch(1);
        final CountDownLatch secondLatch = new CountDownLatch(1);

        final Thread firstThread = new Thread(() -> {
            try {
                semaphore.acquire();
                sharedInt += 1;
                firstLatch.countDown();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            } finally {
                semaphore.release();
            }
        });

        final Thread secondThread = new Thread(() -> {
            try {
                secondLatch.await();
                semaphore.acquire();
                sharedInt += 1;
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            } finally {
                semaphore.release();
            }
        });

        firstThread.start();
        secondThread.start();

        firstLatch.await();
        Assertions.assertEquals(expectedAfterFirstThread, sharedInt);

        secondLatch.countDown();
        secondThread.join();
        Assertions.assertEquals(expectedAfterSecondThread, sharedInt);
    }

    @RepeatedTest(1000)
    public void singleTryAcquirePermitTest() throws InterruptedException {
        final int expectedAfterFirstThread = 1;
        final int expectedAfterSecondThread = 2;

        final BeautySemaphore semaphore = new BeautySemaphore(1);

        final CountDownLatch firstLatch = new CountDownLatch(1);
        final CountDownLatch secondLatch = new CountDownLatch(1);
        final CountDownLatch firstPassLatch = new CountDownLatch(1);
        final CountDownLatch secondPassLatch = new CountDownLatch(1);
        final CountDownLatch checkedLatch = new CountDownLatch(1);

        final Thread firstThread = new Thread(() -> {
            try {
                semaphore.acquire();
                sharedInt += 1;
                firstLatch.countDown();
                secondLatch.await();
            } catch (InterruptedException e) {
                Assertions.fail();
            } finally {
                semaphore.release();
            }
        });

        final Thread secondThread = new Thread(() -> {
            try {
                firstLatch.await();
                firstPassLatch.countDown();
                checkedLatch.await();
                if (!semaphore.tryAcquire()) {
                    secondLatch.countDown();
                }
                semaphore.acquire();
                sharedInt += 1;
                secondPassLatch.countDown();
            } catch (InterruptedException e) {
                Assertions.fail();
            } finally {
                semaphore.release();
            }
        });

        firstThread.start();
        secondThread.start();

        firstPassLatch.await();
        Assertions.assertEquals(expectedAfterFirstThread, sharedInt);
        checkedLatch.countDown();

        secondPassLatch.await();
        secondThread.join();
        Assertions.assertEquals(expectedAfterSecondThread, sharedInt);
    }

    @RepeatedTest(20)
    public void multiAcquirePermitTest() throws InterruptedException {
        final int permits = 5;
        final int threadCount = 100;

        final BeautySemaphore semaphore = new BeautySemaphore(permits);
        final List<Thread> threads = new ArrayList<>();

        final AtomicInteger threadsInProcess = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            threads.add(new Thread(() -> {
                try {
                    semaphore.acquire();
                    Assertions.assertTrue(threadsInProcess.incrementAndGet() <= permits);
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Assertions.fail();
                } finally {
                    threadsInProcess.decrementAndGet();
                    semaphore.release();
                }
            }));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
