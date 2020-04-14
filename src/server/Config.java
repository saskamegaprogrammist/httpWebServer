package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static final String CONFIG_FILE = "/etc/httpd.conf";
    public static final String INDEX_FILE = "index.html";
    public static final int PORT = 8080;

    private Properties config;

    public Config() {
        this.config = new Properties();
        try {
            this.config.load(new FileInputStream(CONFIG_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCpuLimit() {
        int cpu = Integer.parseInt(this.config.getProperty("cpu_limit"));
        if (cpu <= 0) {
            cpu = 1;
        }
        return cpu;
    }

    public int getThreadLimit() {
        return Integer.parseInt(this.config.getProperty("thread_limit"));
    }

    public String getDocumentRoot() {
        return this.config.getProperty("document_root");
    }
}
