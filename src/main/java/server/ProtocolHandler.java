package server;

import common.DnsMessage;
import common.DnsMessageBuilder;
import common.DnsMessageParser;
import config.HandlersConfig;
import service.DnsMessageProcessor;

public abstract class ProtocolHandler {
    private final DnsMessageProcessor processor;

    public ProtocolHandler(DnsMessageProcessor processor) {
        this.processor = processor;
    }

    protected final DnsMessage processRequest(byte[] dnsMessage) {
        DnsMessage request = DnsMessageParser.parse(dnsMessage);
        System.out.printf("Handle request: %s%n", request.getQuestions().get(0).getName());
        return processor.processRequest(request);
    }

    protected abstract void start();
}
