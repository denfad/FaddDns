package service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DNSClient {
    private static final String UPSTREAM_DNS = "8.8.8.8";
    private static final int DNS_TIMEOUT = 5000;
    private final int BUFFER_SIZE = 512;

    public byte[] makeRequest(byte[] requestData) throws Exception{
        DatagramSocket upstreamSocket = new DatagramSocket();
        upstreamSocket.setSoTimeout(DNS_TIMEOUT);

        InetAddress upstreamAddress = InetAddress.getByName(UPSTREAM_DNS);
        DatagramPacket upstreamRequest = new DatagramPacket(
                requestData, requestData.length, upstreamAddress, 53
        );
        upstreamSocket.send(upstreamRequest);

        byte[] responseData = new byte[BUFFER_SIZE];
        DatagramPacket upstreamResponse = new DatagramPacket(responseData, responseData.length);
        upstreamSocket.receive(upstreamResponse);

        return responseData;
    }
}
