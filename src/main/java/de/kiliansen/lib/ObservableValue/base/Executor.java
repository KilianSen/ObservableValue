package de.kiliansen.lib.ObservableValue.base;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Executor is a utility class that manages a thread pool for executing tasks.
 * It allows for dynamic resizing of the thread pool and custom naming strategies for threads.
 * The executor service is designed to be used in a multithreaded environment, particularly for observable values.
 */
public class Executor {
    private static final Object lock = new Object();
    private static volatile Function<ThreadInfo, String> namingStrategy = threadInfo ->
            Executor.class.getSimpleName() + "-Thread-" + threadInfo.id();
    private static final ThreadFactory threadFactory = (Runnable r) -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName(namingStrategy.apply(new ThreadInfo(t.getName(), t.threadId())));
        return t;
    };
    private static volatile ExecutorService executor = newExecutor(Runtime.getRuntime().availableProcessors());
    private static volatile int numThreads = 0;

    protected static ExecutorService getExecutor() {
        return executor;
    }

    /**
     * Sets a custom naming strategy for the threads in the executor service.
     * The naming strategy should be a function that takes a ThreadInfo object and returns a String.
     * This allows for custom thread naming based on the thread's information.
     *
     * @param namingStrategy the function to generate thread names
     */
    public static synchronized void setNamingStrategy(Function<ThreadInfo, String> namingStrategy) {
        if (namingStrategy == null) {
            throw new IllegalArgumentException("Naming strategy cannot be null");
        }
        Executor.namingStrategy = namingStrategy;
    }

    /**
     * Returns the current amount of threads used by the executor service.
     *
     * @return the number of threads in the executor service
     */
    public static int getNumThreads() {
        return numThreads;
    }

    /**
     * Creates a new executor service with the specified number of threads.
     * The number of threads is set to at least 1 to avoid creating an executor with zero threads.
     *
     * @param numThreads the number of threads for the executor service
     *
     * @return a new ExecutorService instance
     */
    private static ExecutorService newExecutor(int numThreads) {
        return Executors.newFixedThreadPool(Math.max(1, numThreads), threadFactory);
    }

    /**
     * Resizes the executor service to a new number of threads.
     * If the number of threads is the same as the current one, no action is taken.
     * If the timeout is reached while waiting for tasks to finish, the old executor is shut down immediately.
     *
     * @param numThreads the new number of threads for the executor service
     * @param timeout    the maximum time to wait for tasks to finish before shutting down
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public static void resize(int numThreads, Duration timeout) throws InterruptedException {
        if (numThreads <= 0) {
            throw new IllegalArgumentException("Number of threads must be greater than 0");
        }
        if (timeout == null || timeout.isNegative()) {
            throw new IllegalArgumentException("Timeout must be positive");
        }

        synchronized (lock) {
            if (numThreads == Executor.numThreads) {
                return; // No change needed
            }

            ExecutorService oldExecutor = executor;

            // Update the static fields atomically
            executor = newExecutor(numThreads);
            Executor.numThreads = numThreads;

            // Shutdown old executor gracefully
            oldExecutor.shutdown();
            if (!oldExecutor.awaitTermination(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
                oldExecutor.shutdownNow();
                // Wait a bit more for tasks to respond to being cancelled
                if (!oldExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate cleanly");
                }
            }
        }
    }

    /**
     * Gracefully shuts down the executor service.
     * Should be called during application shutdown.
     */
    public static void shutdown(Duration timeout) throws InterruptedException {
        if (timeout == null || timeout.isNegative()) {
            throw new IllegalArgumentException("Timeout must be positive");
        }

        synchronized (lock) {
            if (executor.isShutdown()) {
                return;
            }

            executor.shutdown();
            if (!executor.awaitTermination(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate cleanly");
                }
            }
        }
    }

    /**
     * Immediately shuts down the executor service.
     */
    public static void shutdownNow() {
        synchronized (lock) {
            executor.shutdownNow();
        }
    }

    /**
     * A record to hold thread information, including its name and ID.
     * This is used for naming threads in the executor service.
     */
    public record ThreadInfo(String name, long id) {
        public ThreadInfo(Thread thread) {
            this(thread.getName(), thread.threadId());
        }
    }
}
