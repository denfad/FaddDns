package server;

import service.Handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final Handler handler;
    private final int port;
    private ExecutorService threadPool;
    private final int BUFFER_SIZE = 512;


    public Server(Handler handler, int port) {
        this.handler = handler;
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public void run() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("DNS Server started on port " + port);
            while (true) {
                receiveRequest(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveRequest(DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        threadPool.execute(() -> {
            executeRequest(socket, packet);
        });
    }

    private void executeRequest(DatagramSocket socket, DatagramPacket packet) {
        byte[] requestData = packet.getData();
        byte[] responseData = handler.handle(requestData);

        DatagramPacket response = new DatagramPacket(
                responseData,
                responseData.length,
                packet.getAddress(),
                packet.getPort()
        );

        try {
            socket.send(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
