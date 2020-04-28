package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements Runnable{

    // socket for serving client connection
    private ServerSocket socket;

    private int port;
    private volatile boolean isConnected;
   // private ArrayList<ThreadPool> threadPoolArrayList;
    private ThreadPool threadPool;
    private String rootFile;
    private static int threadPoolNumber;
    private static int cpuLimit = 1;


    public Server(Config config) {
        this.port = Config.PORT;
        this.rootFile = config.getDocumentRoot();
        int threadsNumber = config.getThreadLimit();
//        int threadsNumber = Runtime.getRuntime().availableProcessors() + 1;
        threadPool = new ThreadPool(threadsNumber);
        System.out.println(threadsNumber);
//        int threads = config.getThreadLimit();
//        threadPoolArrayList = new ArrayList<>();
//        this.cpuLimit = config.getCpuLimit();
//        for (int i=0; i<this.cpuLimit; i++) {
//            threadPoolArrayList.add(new ThreadPool(threads));
//        }
        this.isConnected = this.createSocketConnection();
        if (this.isConnected) {
            //System.out.println("Server started on port : " + this.port + "\n");
        }
    }

    private boolean createSocketConnection() {
        try {
            this.socket = new ServerSocket(this.port);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int roundRobin() {
        threadPoolNumber = (threadPoolNumber + 1) % cpuLimit;
        return threadPoolNumber;
    }

    @Override
    public void run() {
        if (this.isConnected) {
            for (;;) {
                try {
                    this.threadPool.addTask(new RequestTask(socket.accept(), rootFile));
                    //this.threadPoolArrayList.get(this.roundRobin()).addTask(new RequestTask(socket.accept(), rootFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}