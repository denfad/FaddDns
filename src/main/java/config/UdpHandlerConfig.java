package config;

public class UdpHandlerConfig {
    private Integer port = DEFAULT_PORT;
    private Integer threadsPoolSize = DEFAULT_POOL_SIZE;

    public static final int DEFAULT_PORT = 53;
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
}
