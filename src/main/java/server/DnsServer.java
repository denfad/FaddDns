package server;

import config.AppConfig;
import service.DnsMessageProcessor;
import service.upstream.UpstreamServer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DnsServer {
    private final List<ProtocolHandler> handlers;

    private DnsServer() {
        this.handlers = new ArrayList<>();
    }

    public void addProtocolHandler(ProtocolHandler handler) {
        handlers.add(handler);
    }

    public void start() {
        for(ProtocolHandler handler: handlers) {
            handler.start();
        }
    }

    public static DnsServer initFromConfiguration(AppConfig config) {
        var processor = DnsMessageProcessor.initFromConfig(config.getRecords());
        var server = new DnsServer();
        var upstreams = config.getUpstreams().getValues().stream().map(UpstreamServer::initFromConfig).toList();

        // UDP handler
        if(config.getHandlers().getUdp() != null) {
            var udpHandler = UdpHandler.initFromConfig(processor, config.getHandlers().getUdp());
            // adding upstreams
            upstreams.forEach(udpHandler::addUpstreamServer);
            server.addProtocolHandler(udpHandler);
        }

        return server;
    }
}
