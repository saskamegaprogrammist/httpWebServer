package server;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool implements Executor {

    //first in first out queue, blocking if reading from empty queue or writing in full queue
    private LinkedBlockingQueue<Runnable> tasksQueue;
    //flag for running thread pool
    private volatile boolean isRunning = true;
    //pool of threads size
    private int poolSize = 0;
    //pool of threads
    private ArrayList<ThreadPoolWorker> threadPool;

    public ThreadPool(int poolSize) {
        this.tasksQueue = new LinkedBlockingQueue<Runnable>();
        this.threadPool = new ArrayList<>();
        this.poolSize = poolSize;
        for (int i = 0; i < poolSize; i++) {
            ThreadPoolWorker worker = new ThreadPoolWorker(this);
            Thread newThread = new Thread(worker);
            worker.setThread(newThread);
            newThread.start();
            threadPool.add(worker);
        }
    }

    @Override
    public void execute(Runnable task) {
        if (isRunning) {
            this.tasksQueue.offer(task);
        }
    }

    public void shutdown() {
        this.isRunning = false;
        this.threadPool.forEach(ThreadPoolWorker::stop);
    }

    public boolean IsRunning() {
        return this.isRunning;
    }

    public LinkedBlockingQueue<Runnable> getTasksQueue() {
        return this.tasksQueue;
    }
}
