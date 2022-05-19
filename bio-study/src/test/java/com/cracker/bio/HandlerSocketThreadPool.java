package java.com.cracker.bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HandlerSocketThreadPool {

    private final ExecutorService executor;

    public HandlerSocketThreadPool(final int maxPoolSize, final int queueSize) {
        this.executor = new ThreadPoolExecutor(3, maxPoolSize, 120L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize));
    }

    public void execute(final Runnable task) {
        this.executor.execute(task);
    }
}
