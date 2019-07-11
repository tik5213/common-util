package top.ftas.util.thread;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理类
 */
public class ThreadManager implements ExecutorService {
    ExecutorService mExecutorService;

    public static int STATE_SUCCESS = 200;

    private static class SingletonHolder {
        private static final ThreadManager INSTANCE = new ThreadManager();
    }


    public static ExecutorService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {
        return mExecutorService.awaitTermination(timeout, unit);
    }

    public ThreadManager(ExecutorService executorService) {
        this.mExecutorService = executorService;
    }

    public ThreadManager() {
        //创建一个任务拒绝策略
        //直接忽略新进的任务
        RejectedExecutionHandler rejectedExecutionHandler = new DiscardPolicy();
        //创建一个最大线程数为3的线程池
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 4, 4, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(20), rejectedExecutionHandler);
        //当核心线程空闲时，允许杀死核心线程
        poolExecutor.allowCoreThreadTimeOut(true);
        mExecutorService = poolExecutor;
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

    public <T> Future<T> submit(Callable<T> task) {
        return mExecutorService.submit(task);
    }

    public Future<?> submit(Runnable task) {
        return mExecutorService.submit(task);
    }

    public void execute(Runnable runnable) {
        mExecutorService.execute(runnable);
    }

    public void shutdown() {
        mExecutorService.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return mExecutorService.shutdownNow();
    }

    public boolean isShutdown() {
        return mExecutorService.isShutdown();
    }

    public boolean isTerminated() {
        return mExecutorService.isTerminated();
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return mExecutorService.submit(task, result);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return mExecutorService.invokeAll(tasks);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException {
        return mExecutorService.invokeAll(tasks);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return mExecutorService.invokeAny(tasks);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException {
        return mExecutorService.invokeAny(tasks);
    }
}
