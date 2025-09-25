package service;

import common.DNSMessage;
import common.DNSRecord;

import java.util.List;
import java.util.Optional;

public class Handler {
    private final List<DNSRecord> records;

    public Handler(List<DNSRecord> records) {
        this.records = records;
    }

    public byte[] handle(byte[] requestData) {
        // парсинг запроса
        DNSMessage requestMessage = DNSMessage.createFromByteArray(requestData);
        // ответ на запрос
        DNSMessage responseMessage = processQuery(requestMessage);
        return responseMessage.toByteArray();
    }

    private DNSMessage processQuery(DNSMessage requestMessage) {
        // ищем запись
        Optional<DNSRecord> record = findValue(requestMessage);

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
