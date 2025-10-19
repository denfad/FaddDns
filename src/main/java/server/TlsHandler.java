package server;

import config.TlsHandlerConfig;
import service.DnsMessageProcessor;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TlsHandler extends ProtocolHandler {
    private final int port;
    private final ExecutorService executorService;
    private final String keyStorePath;
    private final String keyStorePassword;
    private final String keyPassword;

    private SSLServerSocket serverSocket;

    private TlsHandler(DnsMessageProcessor processor, TlsHandlerConfig config) {
        super(processor);

        this.port = config.getPort();
        this.keyStorePath = config.getKeyStorePath();
        this.keyStorePassword = config.getKeyStorePassword();
        this.keyPassword = config.getKeyPassword();
        this.executorService = Executors.newFixedThreadPool(config.getThreadsPoolSize());

        try {
            initServerSocket();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("DNS-over-TLS сервер запущен на порту " + port);
        while (!serverSocket.isClosed()) {
            try {
                SSLSocket clientSocket = (SSLSocket) serverSocket.accept();

                executorService.execute(() -> {
                    parseAndExecuteRequest(clientSocket);
                });

            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void parseAndExecuteRequest(SSLSocket clientSocket) {
        try {
            clientSocket.startHandshake();

            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();

            while (!clientSocket.isClosed()) {
                byte[] lengthBytes = new byte[2];
                int bytesRead = input.read(lengthBytes);

                if (bytesRead == -1) {
                    break;
                }

                int messageLength = ((lengthBytes[0] & 0xFF) << 8) | (lengthBytes[1] & 0xFF);

                byte[] dnsMessage = new byte[messageLength];
                readMessage(input, dnsMessage, messageLength);

                byte[] response = processRequest(dnsMessage);

                byte[] responseLength = new byte[2];
                responseLength[0] = (byte) ((response.length >> 8) & 0xFF);
                responseLength[1] = (byte) (response.length & 0xFF);
                output.write(responseLength);

                output.write(response);
                output.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readMessage(InputStream input, byte[] buffer, int length) throws IOException {
        int totalRead = 0;
        while (totalRead < length) {
            int bytesRead = input.read(buffer, totalRead, length - totalRead);
            if (bytesRead == -1) {
                break;
            }
            totalRead += bytesRead;
        }
    }

    private void initServerSocket() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keyStorePath)) {
            keyStore.load(fis, keyStorePassword.toCharArray());
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, keyPassword.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        SSLServerSocketFactory socketFactory = sslContext.getServerSocketFactory();
        serverSocket = (SSLServerSocket) socketFactory.createServerSocket(port);
        serverSocket.setNeedClientAuth(false);
    }

    public static TlsHandler initFromConfig(DnsMessageProcessor processor, TlsHandlerConfig config) {
        return new TlsHandler(processor, config);
    }
}