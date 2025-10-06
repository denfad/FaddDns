package service;

import common.DNSMessage;
import common.DNSRecord;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

public class Handler {
    private final List<DNSRecord> records;
    private final DNSClient dnsClient;

    public Handler(List<DNSRecord> records) {
        this.records = records;
        this.dnsClient = new DNSClient();
    }

    public byte[] handle(byte[] requestData) {
        // парсинг запроса
        DNSMessage requestMessage = DNSMessage.createFromByteArray(requestData);
        // ответ на запрос
        DNSMessage responseMessage = processQuery(requestMessage);
        // если не нашли у себя домен, то перенаправляем к вышестоящему серверу
        if(responseMessage.getAnswerCount() == 0) {
            try {
                return dnsClient.makeRequest(requestData);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return responseMessage.toByteArray();
    }

    private DNSMessage processQuery(DNSMessage requestMessage) {
        // ищем запись
        Optional<DNSRecord> record = findValue(requestMessage);
        System.out.println("Request: " + requestMessage.getQuestionName());

        // формируем пустой ответ
        DNSMessage response = DNSMessage.createEmptyResponseFromRequest(requestMessage);

        // наполянем ответ
        record.ifPresent(response::addAnswer);

        return response;
    }

    private Optional<DNSRecord> findValue(DNSMessage requestMessage) {
        return records.stream()
                .filter(record -> record.getDomain().equals(requestMessage.getQuestionName()))
                .findFirst();
    }
}
