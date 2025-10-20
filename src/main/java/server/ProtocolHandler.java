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

    public abstract Protocol getProtocol();

    public void addUpstreamServer(UpstreamServer server) {
        upstreamServers.add(server);
    }

    protected final byte[] processRequest(byte[] dnsMessage) {
        System.out.println("Receive request on " + getProtocol() + " handler");
        try {
            DnsMessage request = DnsMessageParser.parse(dnsMessage);
            System.out.printf("Handle request: %s \n", request);
            var response = processor.processRequest(request);

            // если не нашли в локальном хранилище
            if (response.getResponseCode() == DnsResponseCode.NOT_FOUND) {
                System.out.printf("Use upstream for request: %s \n", request);
                return processRequestInUpstream(dnsMessage);
            }

            return DnsMessageWriter.write(response);
        } catch (Exception e) {
            // В случае любых ошибок (парсинга и т.п.) пересылаем вышестоящим серверам,
            // надеясь на то что они знаю что делать с запросом
            System.err.printf("Invalid message, use upstream \n");
            return processRequestInUpstream(dnsMessage);
        }
    }

    private byte[] processRequestInUpstream(byte[] dnsMessage) {
        for (UpstreamServer server : upstreamServers) {
            try {
                return server.processRequest(dnsMessage);
            } catch (Exception e) {
                // ignore, next server
            }
        }

        return dnsMessage;
    }
}
