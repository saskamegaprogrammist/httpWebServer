import server.Config;
import server.Server;
import server.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;

public class Main{

    public static void main(String[] args) {
        Config config = new Config();
        Server server = new Server(config);
        server.run();
    }
}