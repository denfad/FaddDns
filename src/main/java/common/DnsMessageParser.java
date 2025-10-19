package common;

import java.nio.ByteBuffer;

public class DnsMessageParser {

    public static DnsMessage parse(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        DnsMessage message = parseHeader(buffer);

        int questionCount = buffer.getShort() & 0xFFFF;

        // TODO parse resource records
        int anCount = buffer.getShort() & 0xFFFF;
        int nsCount = buffer.getShort() & 0xFFFF;
        int arCount = buffer.getShort() & 0xFFFF;

        parseQuestions(questionCount, buffer, message);
        return message;
    }

    private static DnsMessage parseHeader(ByteBuffer buffer) {
        int id = buffer.getShort() & 0xFFFF;

        int flags = buffer.getShort() & 0xFFFF;
        DnsMessageType messageType = DnsMessageType.fromValue((flags >> 15) & 0x1);
        DnsOpCode opCode = DnsOpCode.fromValue((flags >> 11) & 0xF);
        boolean authoritativeAnswer = ((flags >> 10) & 0x1) == 1;
        boolean truncated = ((flags >> 9) & 0x1) == 1;
        boolean recursionDesired = ((flags >> 8) & 0x1) == 1;
        boolean recursionAvailable = ((flags >> 7) & 0x1) == 1;
        DnsResponseCode responseCode = DnsResponseCode.fromValue(flags & 0xF);

        DnsMessage message = new DnsMessage(id, messageType, opCode);
        message.setAuthoritativeAnswer(authoritativeAnswer);
        message.setTruncated(truncated);
        message.setRecursionDesired(recursionDesired);
        message.setRecursionAvailable(recursionAvailable);
        message.setResponseCode(responseCode);

        return message;
    }

    private static void parseQuestions(int questionCount, ByteBuffer buffer, DnsMessage message) {
        for (int i = 0; i < questionCount; i++) {
            String name = readDomainName(buffer);
            DnsRecordType type = DnsRecordType.fromValue(buffer.getShort() & 0xFFFF);
            DnsRecordClass recordClass = DnsRecordClass.fromValue(buffer.getShort() & 0xFFFF);

            message.addQuestion(new DnsQuestion(name, type, recordClass));
        }
    }

    private static String readDomainName(ByteBuffer buffer) {
        StringBuilder name = new StringBuilder();
        int length;

        while ((length = buffer.get() & 0xFF) != 0) {
            if ((length & 0xC0) == 0xC0) {
                buffer.get();
                break;
            }

            byte[] label = new byte[length];
            buffer.get(label);
            if (name.length() > 0) {
                name.append(".");
            }
            name.append(new String(label));
        }

        return name.toString();
    }

}
