package server;

import java.util.HashMap;

public class ExtensionResolver {
    private static ExtensionResolver instance;
    private HashMap<String, String> map ;

    private static final String[] extensions = {"css", "html", "js", "jpg", "jpeg", "png", "gif", "swf"};
    private static final String[] contentTypes = {"text/css", "text/html", "application/javascript", "image/jpeg", "image/jpeg",  "image/png", "image/gif", "application/x-shockwave-flash"};

    private static final String DEFAULT_CT = "text/plain";

    public ExtensionResolver() {
        map = new HashMap<>();
        int i=0;
        for (String ext : extensions) {
            map.put(ext, contentTypes[i++]);
        }
    }

    public static ExtensionResolver getInstance(){
        if(instance == null){
            instance = new ExtensionResolver();
        }
        return instance;
    }

    synchronized public String getCT(String ext) {
        return map.getOrDefault(ext, DEFAULT_CT);
    }

}
