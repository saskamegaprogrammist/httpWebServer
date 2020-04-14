package server;

import java.net.Socket;

public class RequestTask implements Runnable {

    private Socket clientSocket;
    private String filesRoot;

    public RequestTask(Socket socket, String root) {
        //System.out.println(socket.getInetAddress());
        this.clientSocket = socket;
        this.filesRoot = root;
    }


    @Override
    public void run() {
        RequestsProcessor processor = new RequestsProcessor(clientSocket, filesRoot);
        processor.run();
    }
}
