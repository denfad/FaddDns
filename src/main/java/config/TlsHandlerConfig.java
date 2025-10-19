package config;

public class TlsHandlerConfig {
    private Integer port = DEFAULT_PORT;
    private Integer threadsPoolSize = DEFAULT_POOL_SIZE;
    private String keyStorePath;
    private String keyStorePassword;
    private String keyPassword;

    public static final int DEFAULT_PORT = 853;
    public static final int DEFAULT_POOL_SIZE = 5;
    public static int BUFFER_SIZE = 512;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getThreadsPoolSize() {
        return threadsPoolSize;
    }

    public void setThreadsPoolSize(Integer threadsPoolSize) {
        this.threadsPoolSize = threadsPoolSize;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }
}
