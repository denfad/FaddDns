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
    private static final String UPSTREAM_DNS = "8.8.8.8";

    public Handler(List<DNSRecord> records) {
        this.records = records;
    }

    public byte[] handle(byte[] requestData) {
        // парсинг запроса
        DNSMessage requestMessage = DNSMessage.createFromByteArray(requestData);
        // ответ на запрос
        DNSMessage responseMessage = processQuery(requestMessage);
        // если не нашли у себя домен, то перенаправляем к вышестоящему серверу
        if(responseMessage.getAnswerCount() == 0) {
            try {
                return findInUpStream(requestData);
            } catch (Exception e) {
                throw new RuntimeException(e);
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

    private byte[] findInUpStream(byte[] requestData) throws Exception{
        // Создаем сокет для отправки запроса к вышестоящему DNS
        DatagramSocket upstreamSocket = new DatagramSocket();
        upstreamSocket.setSoTimeout(5000); // Таймаут 5 секунд

        // Отправляем запрос к вышестоящему DNS-серверу
        InetAddress upstreamAddress = InetAddress.getByName(UPSTREAM_DNS);
        DatagramPacket upstreamRequest = new DatagramPacket(
                requestData, requestData.length, upstreamAddress, 53
        );
        upstreamSocket.send(upstreamRequest);

        // Получаем ответ от вышестоящего DNS-сервера
        byte[] responseData = new byte[1024];
        DatagramPacket upstreamResponse = new DatagramPacket(responseData, responseData.length);
        upstreamSocket.receive(upstreamResponse);

        return responseData;
    }

    private Optional<DNSRecord> findValue(DNSMessage requestMessage) {
        return records.stream()
                .filter(record -> record.getDomain().equals(requestMessage.getQuestionName()))
                .findFirst();
    }
}
