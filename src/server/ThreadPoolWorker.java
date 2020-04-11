package server;

import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPoolWorker implements Runnable {
    //thread that executes tasks
    private Thread thread;
    //threadpools queue
    private LinkedBlockingQueue<Runnable> tasksQueue ;
    //link to thread pool
    private ThreadPool threadPool;

    public ThreadPoolWorker(ThreadPool parentThreadPool) {
        this.threadPool = parentThreadPool;
        this.tasksQueue = parentThreadPool.getTasksQueue();
    }

    @Override
    public void run() {
        while (threadPool.IsRunning()) {
            try {
                Runnable nextTask = tasksQueue.poll();
                if (nextTask != null) {
                    nextTask.run();
                }
            } catch (Exception e) {
                e.printStackTrace(); //TODO : logger
            }
        }
    }

    public void setThread(Thread thread) {
        this. thread = thread;
    }

    public Thread getThread() {
        return this.thread;
    }

    public void stop() {
        this.thread.interrupt();
    }
}
