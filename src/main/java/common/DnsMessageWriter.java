package common;

import java.nio.ByteBuffer;
import java.util.List;

public class DnsMessageWriter {
    private static final int BUFFER_SIZE = 1024;

    public static byte[] write(DnsMessage message) {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        writeHeader(buffer, message);
        writeQuestions(buffer, message);
        writeResourceRecords(buffer, message.getAnswers());
        writeResourceRecords(buffer, message.getAuthorityRecords());
        writeResourceRecords(buffer, message.getAdditionalRecords());

        byte[] result = new byte[buffer.position()];
        buffer.flip();
        buffer.get(result);
        return result;
    }

    private static void writeHeader(ByteBuffer buffer, DnsMessage message) {
        buffer.putShort((short) message.getId());

        int flags = 0;
        flags |= (message.getMessageType().getValue() & 0x1) << 15; // QR
        flags |= (message.getOpCode().getValue() & 0xF) << 11;      // OPCODE
        flags |= (message.isAuthoritativeAnswer() ? 1 : 0) << 10;   // AA
        flags |= (message.isTruncated() ? 1 : 0) << 9;              // TC
        flags |= (message.isRecursionDesired() ? 1 : 0) << 8;       // RD
        flags |= (message.isRecursionAvailable() ? 1 : 0) << 7;     // RA
        flags |= (message.getResponseCode().getValue() & 0xF);      // RCODE

        buffer.putShort((short) flags);

        buffer.putShort((short) message.getQuestions().size());
        buffer.putShort((short) message.getAnswers().size());
        buffer.putShort((short) message.getAuthorityRecords().size());
        buffer.putShort((short) message.getAdditionalRecords().size());
    }

    private static void writeQuestions(ByteBuffer buffer, DnsMessage message) {
        for (DnsQuestion question : message.getQuestions()) {
            writeDomainName(buffer, question.getName());
            buffer.putShort((short) question.getType().getValue());
            buffer.putShort((short) question.getRecordClass().getValue());
        }
    }

    private static void writeResourceRecords(ByteBuffer buffer, List<DnsResourceRecord> records) {
        for (DnsResourceRecord record : records) {
            writeDomainName(buffer, record.getName());
            buffer.putShort((short) record.getType().getValue());
            buffer.putShort((short) record.getRecordClass().getValue());
            buffer.putInt((int) record.getTtl());

            byte[] data = record.getData();
            buffer.putShort((short) data.length);
            buffer.put(data);
        }
    }


    private static void writeDomainName(ByteBuffer buffer, String name) {
        String[] labels = name.split("\\.");
        for (String label : labels) {
            buffer.put((byte) label.length());
            buffer.put(label.getBytes());
        }
        buffer.put((byte) 0);
    }
}
