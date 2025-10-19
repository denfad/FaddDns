package common;


public class DnsMessageBuilder {

    public static DnsMessage createResponse(DnsMessage request) {
        return createResponse(request, DnsResponseCode.NO_ERROR);
    }

    public static DnsMessage createResponse(DnsMessage request, DnsResponseCode responseCode) {
        DnsMessage response = new DnsMessage(request.getId(), DnsMessageType.RESPONSE, request.getOpCode());
        response.setRecursionDesired(request.isRecursionDesired());
        response.setRecursionAvailable(true);
        response.setResponseCode(responseCode);

        // Копируем вопросы из запроса
        for (DnsQuestion question : request.getQuestions()) {
            response.addQuestion(question);
        }

        return response;
    }

    public static DnsMessage createErrorResponse(DnsMessage request, DnsResponseCode errorCode) {
        DnsMessage response = createResponse(request, errorCode);
        response.setAuthoritativeAnswer(false);
        return response;
    }

    public static DnsResourceRecord createResourceRecord(String domain,
                                                         DnsRecordType type,
                                                         DnsRecordClass recordClass,
                                                         byte[] value) {
        if (value == null) return null;
        long ttl = 300;
        return new DnsResourceRecord(domain, type, recordClass, ttl, value);
    }
}
