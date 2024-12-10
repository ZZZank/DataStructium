package zank.mods.datastructium.utils.pool;

import io.netty.util.internal.shaded.org.jctools.queues.MpscUnboundedArrayQueue;
import lombok.val;
import zank.mods.datastructium.DataStructium;

import java.util.Queue;
import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public final class AsyncPool<T> {

    private final Queue<T> values = new MpscUnboundedArrayQueue<>(10_000);
    private final Queue<Consumer<T>> accepters = new MpscUnboundedArrayQueue<>(10_000);
    private final String name;
    private Worker worker;
    private final DeduplicatingPool<T> backend = new DeduplicatingPool<>();

    public AsyncPool(String name) {
        this.name = DataStructium.MOD_ID + "async-pool-" + name;
        start();
    }

    public synchronized void offer(final T value, final Consumer<T> resultAccepter) {
        val added = values.offer(value);
        if (added) {
            accepters.add(resultAccepter);
        }
    }

    public void start() {
        if (isRunning()) {
            return;
        }
        stop();
        worker = new Worker();
        worker.start();
    }

    public void stop() {
        if (worker != null) {
            worker.interrupt();
            worker = null;
        }
    }

    public boolean isRunning() {
        return worker != null && worker.isAlive();
    }

    class Worker extends Thread {
        Worker() {
            super(name + "-worker");
        }

        @Override
        public void run() {
            DataStructium.LOGGER.info("worker '{}' started", this.getName());
            Consumer<T> accepter;
            T value;
            while (!Thread.currentThread().isInterrupted()) {
                while ((accepter = accepters.poll()) != null && (value = values.poll()) != null) {
                    accepter.accept(backend.unique(value));
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    DataStructium.LOGGER.info("worker '{}' interrupted", this.getName());
                    break;
                }
            }
            DataStructium.LOGGER.info("worker '{}' stopped", this.getName());
        }
    }
}
