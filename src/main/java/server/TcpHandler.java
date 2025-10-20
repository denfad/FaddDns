package server;

import config.TcpHandlerConfig;
import config.UdpHandlerConfig;
import service.DnsMessageProcessor;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpHandler extends ProtocolHandler {
    private final int port;
    private final ExecutorService executorService;

    private TcpHandler(DnsMessageProcessor processor, TcpHandlerConfig config) {
        super(processor);

        this.port = config.getPort();
        this.executorService = Executors.newFixedThreadPool(config.getThreadsPoolSize());
    }

    @Override
    public void run() {
        try (ServerSocket socket = new ServerSocket(port)) {
            System.out.println("TCP Server started on port " + port);
            while (true) {
                receiveRequest(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void receiveRequest(ServerSocket socket) throws IOException {
        Socket server = socket.accept();

        executorService.execute(() -> {
            parseAndExecuteRequest(server);
        });
    }


    private void parseAndExecuteRequest(Socket clientSocket) {
        try {

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

    public static TcpHandler initFromConfig(DnsMessageProcessor processor, TcpHandlerConfig config) {
        return new TcpHandler(processor, config);
    }
}
