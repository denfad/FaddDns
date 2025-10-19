package service.upstream;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpDnsClient {
    private final String address;
    private final int port;

    private final int BUFFER_SIZE = 512;
    private final int DNS_TIMEOUT = 5000;

    public UdpDnsClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public byte[] makeRequest(byte[] requestData) throws Exception{
        DatagramSocket upstreamSocket = new DatagramSocket();
        upstreamSocket.setSoTimeout(DNS_TIMEOUT);

        InetAddress upstreamAddress = InetAddress.getByName(address);
        DatagramPacket upstreamRequest = new DatagramPacket(
                requestData, requestData.length, upstreamAddress, port
        );
        upstreamSocket.send(upstreamRequest);

        byte[] responseData = new byte[BUFFER_SIZE];
        DatagramPacket upstreamResponse = new DatagramPacket(responseData, responseData.length);
        upstreamSocket.receive(upstreamResponse);

        return responseData;
    }
}
