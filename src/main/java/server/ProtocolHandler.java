package server;

import common.*;
import config.HandlersConfig;
import service.DnsMessageProcessor;
import service.upstream.UpstreamServer;

import java.util.ArrayList;
import java.util.List;

public abstract class ProtocolHandler implements Runnable{
    private final DnsMessageProcessor processor;
    private final List<UpstreamServer> upstreamServers = new ArrayList<>();

    public ProtocolHandler(DnsMessageProcessor processor) {
        this.processor = processor;
    }

    public void addUpstreamServer(UpstreamServer server) {
        upstreamServers.add(server);
    }

    protected final byte[] processRequest(byte[] dnsMessage) {
        DnsMessage request = DnsMessageParser.parse(dnsMessage);
        System.out.printf("Handle request: %s \n", request);

        var response = processor.processRequest(request);

        // если не нашли в локальном хранилище
        if(response.getResponseCode() == DnsResponseCode.NOT_FOUND) {
            for(UpstreamServer server: upstreamServers) {
                try {
                    return server.processRequest(dnsMessage);
                } catch (Exception e) {
                    // ignore, next server
                }
            }
        }

        return DnsMessageWriter.write(response);
    }
}
