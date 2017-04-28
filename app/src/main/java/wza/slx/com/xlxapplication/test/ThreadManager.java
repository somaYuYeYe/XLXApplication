package wza.slx.com.xlxapplication.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by homelink on 2017/4/5.
 */
public class ThreadManager {
    private static ThreadManager ourInstance = new ThreadManager();

    public static ThreadManager getInstance() {
        return ourInstance;
    }

    private ExecutorService cachedThreadPool;

    private ThreadManager() {
        cachedThreadPool = Executors.newCachedThreadPool();
        // cachedThreadPool = Executors.newScheduledThreadPool()
        cachedThreadPool = Executors.newFixedThreadPool(4);
        // cachedThreadPool = new ThreadPoolExecutor(1, 4, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(4));
        cachedThreadPool = new ThreadPoolExecutor(0, 6, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public void execute(Runnable task) {
        cachedThreadPool.execute(task);
    }
}
