package service.upstream;

import config.UpstreamConfig;

public class UpstreamServer {
    private final UdpDnsClient dnsClient;

    private UpstreamServer(String address, int port) {
        this.dnsClient = new UdpDnsClient(address, port);
    }

    public byte[] processRequest(byte[] request) throws Exception {
        return dnsClient.makeRequest(request);
    }

    public static UpstreamServer initFromConfig(UpstreamConfig config) {
        return new UpstreamServer(config.getAddress(), config.getPort());
    }
}
