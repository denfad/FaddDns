package server;

import common.DnsMessage;
import common.DnsMessageBuilder;
import common.DnsMessageParser;
import common.DnsMessageWriter;
import config.UdpHandlerConfig;
import service.DnsMessageProcessor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UdpHandler extends ProtocolHandler {
    private final int port;
    private final ExecutorService executorService;

    private UdpHandler(DnsMessageProcessor processor, UdpHandlerConfig config) {
        super(processor);

        this.port = config.getPort();
        this.executorService = Executors.newFixedThreadPool(config.getThreadsPoolSize());
    }

    @Override
    protected void start() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server started on port " + port);
            while (true) {
                receiveRequest(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void receiveRequest(DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[UdpHandlerConfig.BUFFER_SIZE];

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        executorService.execute(() -> {
            parseAndExecuteRequest(socket, packet);
        });
    }


    private void parseAndExecuteRequest(DatagramSocket socket, DatagramPacket requestPacket) {
        byte[] requestData = requestPacket.getData();
        DnsMessage response = processRequest(requestData);
        byte[] responseData = DnsMessageWriter.write(response);

        DatagramPacket responsePacket = new DatagramPacket(
                responseData,
                responseData.length,
                requestPacket.getAddress(),
                requestPacket.getPort()
        );

        try {
            socket.send(responsePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UdpHandler initFromConfig(DnsMessageProcessor processor, UdpHandlerConfig config) {
        return new UdpHandler(processor, config);
    }
}
