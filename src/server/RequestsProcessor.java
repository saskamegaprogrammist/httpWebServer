package server;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class RequestsProcessor implements Runnable {
    // input stream from client
    private BufferedReader inputReader;
    // output stream for headers
    private PrintWriter headersOutputWriter;
    // output stream for body
    private BufferedOutputStream bodyOutputWriter;
    private static final String GET_METHOD = "GET";
    private static final String HEAD_METHOD = "HEAD";
    private static  String FILES_ROOT = ".";

    public RequestsProcessor(Socket socket, String root) {
        try {
            FILES_ROOT += root;
            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bodyOutputWriter = new BufferedOutputStream(socket.getOutputStream());
            headersOutputWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String input = inputReader.readLine();
            StringTokenizer parse = new StringTokenizer(input); //list of tokens
            String method = parse.nextToken().toUpperCase();

            System.out.println(method);

            String fileRequested = parse.nextToken().toLowerCase();

            if (!checkMethod(method)) {
                create405Answer();
            } else {
                if (fileRequested.endsWith("/")) {
                    fileRequested += Config.INDEX_FILE;
                }

                File file = new File(FILES_ROOT, fileRequested);

                if (methodGet(method)) {

                    int fileLength = (int) file.length();
                    byte[] fileData = readFileData(file, fileLength);

                    headersOutputWriter.println("HTTP/1.1 200 OK");
                    headersOutputWriter.println("Server: Java Thread Pool Server by Alexis");
                    headersOutputWriter.println("Date: " + new Date());
                    headersOutputWriter.println("Content-type: " +  getContentType(fileRequested));
                    headersOutputWriter.println("Content-length: " + fileLength);
                    headersOutputWriter.println(); // blank line between headers and content
                    headersOutputWriter.flush(); // flush character output stream buffer

                    bodyOutputWriter.write(fileData, 0, fileLength);
                    bodyOutputWriter.flush();
                }

            }
        } catch (FileNotFoundException e) {
                create404Answer();
        } catch (IOException e) {
                e.printStackTrace();
            }

    }


    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }


    private boolean checkMethod(String method) {
        return methodGet(method) || methodHead(method);
    }

    private boolean methodGet(String method) {
        return method.equals(GET_METHOD);
    }

    private boolean methodHead(String method) {
        return method.equals(HEAD_METHOD);
    }

    private String getContentType(String fileRequested) {
        String[] fileSplit = fileRequested.split("\\.");
        String extension = fileSplit[fileSplit.length-1];
        ExtensionResolver extensionResolver = new ExtensionResolver();
        return extensionResolver.getCT(extension);
    }

    private void create405Answer() {
        headersOutputWriter.println("HTTP/1.1 405 Not Implemented");
        headersOutputWriter.println();
        headersOutputWriter.flush();
    }
    private void create404Answer() {
        headersOutputWriter.println("HTTP/1.1 404 File Not Found");
        headersOutputWriter.println();
        headersOutputWriter.flush();

    }

}
