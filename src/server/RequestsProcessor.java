package server;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Date;
import java.util.StringTokenizer;

public class RequestsProcessor implements Runnable {
    // input stream from client
    private BufferedReader inputReader;
    // output stream
    private BufferedOutputStream outputWriter;
    private static final String GET_METHOD = "GET";
    private static final String HEAD_METHOD = "HEAD";
    private static final int TIMEOUT = 10000;
    private String FILES_ROOT = "";
    private boolean dirRequest = false;

    public RequestsProcessor(Socket socket, String root) {
        try {
            socket.setSoTimeout(TIMEOUT);
            FILES_ROOT += root;
            OutputStream outStream  = socket.getOutputStream();
            InputStream inputStream  = socket.getInputStream();
            inputReader = new BufferedReader(new InputStreamReader(inputStream));
            outputWriter = new BufferedOutputStream(outStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String input = inputReader.readLine();
            if (input == null) {
                create404Answer();
                return;
            }
            StringTokenizer parse = new StringTokenizer(input); //list of tokens

            if (!parse.hasMoreTokens())  {
                create404Answer();
                return;
            };
            String method = parse.nextToken().toUpperCase();


            String fileRequested = parse.nextToken().toLowerCase();
            fileRequested = URLDecoder.decode( fileRequested, "UTF-8" );

            if (checkFileEscaping(fileRequested)) {
                create403Answer();
                return;
            }

            fileRequested = deleteParams(fileRequested);


            if (!checkMethod(method)) {
                create405Answer();
            } else {
                if (fileRequested.endsWith("/")) {
                    dirRequest = true;
                    fileRequested += Config.INDEX_FILE;
                }

                File file = new File(FILES_ROOT, fileRequested);

                int fileLength = (int) file.length();
                byte[] fileData = readFileData(file);

                if (methodGet(method)) {
                    String headers = "HTTP/1.1 200 OK \nServer: Java Thread Pool Server by Alexis\nDate: " + new Date() +"\nContent-type: " + getContentType(fileRequested) + "\nContent-length: " + fileLength +"\n\n";

                    outputWriter.write(headers.getBytes());

                    outputWriter.write(fileData, 0, fileLength);
                    outputWriter.flush();
                } else {
                    String headers = "HTTP/1.1 200 OK\r\nServer: Java Thread Pool Server by Alexis\r\nDate: " + new Date() +"\r\nContent-type: " + getContentType(fileRequested) + "\r\nContent-Length: " + fileLength +"\r\n\r\n";
                    outputWriter.write(headers.getBytes());
                    outputWriter.flush();

                }
            }
        } catch (NoSuchFileException e) {
            try {
                if (dirRequest) {
                    create403Answer();
                } else {
                    create404Answer();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputReader.close();
                outputWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private byte[] readFileData(File file) throws IOException {
        FilesCache filesCache = FilesCache.getInstance();
        Path filename = file.toPath();
        String filePath = filename.toString();
        byte[] outputFile = filesCache.get(filename.toString());
        if (outputFile == null) {
            outputFile = Files.readAllBytes(filename);
            filesCache.put(filePath, outputFile);
        }
        return outputFile;
    }

    private boolean checkFileEscaping(String file) {
        return file.contains("../");
    }

    private String deleteParams(String file) {
        return file.split("\\?")[0];
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
        ExtensionResolver extensionResolver = ExtensionResolver.getInstance();
        return extensionResolver.getCT(extension);
    }

    private void create405Answer() throws IOException {
        String headers = "HTTP/1.1 405 Not Implemented\nServer: Java Thread Pool Server by Alexis\nDate: " + new Date() +"\n";
        outputWriter.write(headers.getBytes());
        outputWriter.flush();
    }

    private void create403Answer() throws IOException {
        String headers = "HTTP/1.1 403 Forbidden\nServer: Java Thread Pool Server by Alexis\nDate: " + new Date() +"\n";
        outputWriter.write(headers.getBytes());
        outputWriter.flush();

    }
    private void create404Answer() throws IOException {

        String headers = "HTTP/1.1 404 File Not Found\nServer: Java Thread Pool Server by Alexis\nDate: " + new Date() +"\n";
        outputWriter.write(headers.getBytes());
        outputWriter.flush();


    }



}
