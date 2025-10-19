package service;

import common.*;
import config.RecordsConfig;
import service.storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class DnsMessageProcessor {
    private final Storage storage;

    private DnsMessageProcessor(Storage storage) {
        this.storage = storage;
    }

    public DnsMessage processRequest(DnsMessage request) {
        var response = DnsMessageBuilder.createResponse(request);

        for(DnsQuestion question: request.getQuestions()) {
            var answers = processQuestion(question);

            if(answers.isEmpty()) {
                DnsMessageBuilder.createErrorResponse(request, DnsResponseCode.NOT_FOUND);
            } else {
                answers.forEach(response::addAnswer);
            }
        }

        return response;
    }

    public List<DnsResourceRecord> processQuestion(DnsQuestion question) {
        var record = storage.getRecord(question.getType(), question.getName());
        var answers = new ArrayList<DnsResourceRecord>();

        if (record != null)  {
            record.getValues().forEach(value -> {
                byte[] byteValue = DnsMessageUtils.toByteArray(value, record.getType());

                var resourceRecord = DnsMessageBuilder.createResourceRecord(
                        question.getName(),
                        question.getType(),
                        question.getRecordClass(),
                        byteValue);

                answers.add(resourceRecord);
            });
        }

        return answers;
    }

    public static DnsMessageProcessor initFromConfig(RecordsConfig recordsConfig) {
        var storage = Storage.initFromConfiguration(recordsConfig);
        return new DnsMessageProcessor(storage);
    }
}
