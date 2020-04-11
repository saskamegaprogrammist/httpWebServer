package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server implements Runnable{

    // socket for serving client connection
    private ServerSocket socket;

    private int port;
    private boolean isConnected;
    private ThreadPool threadPool;
    private String rootFile;


    public Server(Config config) {
        this.port = Config.PORT;
        this.rootFile = config.getDocumentRoot();
        threadPool = new ThreadPool(config.getThreadLimit());
        this.isConnected = this.createSocketConnection();
        if (this.isConnected) {
            System.out.println("Server started on port : " + this.port + "\n");
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

    @Override
    public void run() {
        if (this.isConnected) {
            for (;;) {
                try {
                    this.threadPool.execute(new RequestTask(socket.accept(), rootFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}