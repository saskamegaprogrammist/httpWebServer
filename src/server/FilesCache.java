package server;

import java.util.HashMap;

public class FilesCache {
    private static FilesCache instance;
    private HashMap<String, byte[] > map ;
    public FilesCache() {
        map = new HashMap<>();
    }

    public static FilesCache getInstance(){
        if(instance == null){
            instance = new FilesCache();
        }
        return instance;
    }

    synchronized public void put(String filename, byte[]  file) {
        map.put(filename, file);
    }

    synchronized public byte[] get(String filename) {
        return map.getOrDefault(filename, null);
    }

}
