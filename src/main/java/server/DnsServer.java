package server;

import config.AppConfig;
import service.DnsMessageProcessor;

import java.util.ArrayList;
import java.util.List;

public class DnsServer {
    private final List<ProtocolHandler> handlers;

    private DnsServer(List<ProtocolHandler> handlers) {
        this.handlers = handlers;
    }

    public void start() {
        for(ProtocolHandler handler: handlers) {
            handler.start();
        }
    }

    public static DnsServer initFromConfiguration(AppConfig config) {
        var processor = DnsMessageProcessor.initFromConfig(config.getRecords());

        var handlers = new ArrayList<ProtocolHandler>();
        if(config.getHandlers().getUdp() != null) {
            var udpHandler = UdpHandler.initFromConfig(processor, config.getHandlers().getUdp());
            handlers.add(udpHandler);
        }

        return new DnsServer(handlers);
    }
}
