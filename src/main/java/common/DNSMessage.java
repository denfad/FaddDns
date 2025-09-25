package common;

import java.nio.ByteBuffer;

public class DNSMessage {
    private static int FLAGS_NOT_FOUND = 0x8183; // NXDOMAIN
    private static int FLAGS_FOUND = 0x8180; // QR=1, OPCODE=0, AA=1, RD=1, RA=1, RCODE=0

    private int id;
    private int flags;
    private int questionCount;
    private int answerCount;
    private int authorityCount;
    private int additionalCount;

    private String questionName;
    private int questionType;
    private int questionClass;

    private String answerName;
    private int answerType;
    private int answerClass;
    private int answerTtl;
    private String answerData;


    private DNSMessage(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        this.id = buffer.getShort() & 0xFFFF;
        this.flags = buffer.getShort() & 0xFFFF;
        this.questionCount = buffer.getShort() & 0xFFFF;
        this.answerCount = buffer.getShort() & 0xFFFF;
        this.authorityCount = buffer.getShort() & 0xFFFF;
        this.additionalCount = buffer.getShort() & 0xFFFF;

        this.questionName = readDomainName(buffer);
        this.questionType = buffer.getShort() & 0xFFFF;
        this.questionClass = buffer.getShort() & 0xFFFF;
    }

    private DNSMessage() {
    }

    public static DNSMessage createFromByteArray(byte[] data) {
        return new DNSMessage(data);
    }

    public static DNSMessage createEmptyResponseFromRequest(DNSMessage requestMessage) {
        DNSMessage message = new DNSMessage();
        message.id = requestMessage.id;
        message.flags = FLAGS_NOT_FOUND;
        message.questionCount = requestMessage.questionCount;
        message.answerCount = 0;
        message.authorityCount = 0;
        message.additionalCount = 0;

        message.questionName = requestMessage.questionName;
        message.questionType = requestMessage.questionType;
        message.questionClass = requestMessage.questionClass;

        return message;
    }

    public void addAnswer(DNSRecord record) {
        this.setFlags(FLAGS_FOUND);
        this.setAnswerName(record.getDomain());
        this.setAnswerCount(1);
        this.setAnswerType(1); // A
        this.setAnswerClass(1); // IN
        this.setAnswerTtl(300);
        this.setAnswerData(record.getValue());
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(512);

        buffer.putShort((short) id);
        buffer.putShort((short) flags);
        buffer.putShort((short) questionCount);
        buffer.putShort((short) answerCount);
        buffer.putShort((short) authorityCount);
        buffer.putShort((short) additionalCount);

        writeDomainName(buffer, questionName);
        buffer.putShort((short) questionType);
        buffer.putShort((short) questionClass);

        if(answerData != null && !answerData.isEmpty()) {
            writeDomainName(buffer, answerName);
            buffer.putShort((short) answerType);
            buffer.putShort((short) answerClass);
            buffer.putInt(answerTtl);

            String[] ipParts = answerData.split("\\.");
            buffer.putShort((short) 4);
            for (String part : ipParts) {
                buffer.put((byte) Integer.parseInt(part));
            }
        }

        byte[] result = new byte[buffer.position()];
        buffer.flip();
        buffer.get(result);
        return result;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFlags() { return flags; }
    public void setFlags(int flags) { this.flags = flags; }

    public int getQuestionCount() { return questionCount; }
    public void setQuestionCount(int questionCount) { this.questionCount = questionCount; }

    public int getAnswerCount() { return answerCount; }
    public void setAnswerCount(int answerCount) { this.answerCount = answerCount; }

    public int getAuthorityCount() { return authorityCount; }
    public void setAuthorityCount(int authorityCount) { this.authorityCount = authorityCount; }

    public int getAdditionalCount() { return additionalCount; }
    public void setAdditionalCount(int additionalCount) { this.additionalCount = additionalCount; }

    public String getQuestionName() { return questionName; }
    public void setQuestionName(String questionName) { this.questionName = questionName; }

    public int getQuestionType() { return questionType; }
    public void setQuestionType(int questionType) { this.questionType = questionType; }

    public int getQuestionClass() { return questionClass; }
    public void setQuestionClass(int questionClass) { this.questionClass = questionClass; }

    public void setAnswerName(String answerName) { this.answerName = answerName; }
    public void setAnswerType(int answerType) { this.answerType = answerType; }
    public void setAnswerClass(int answerClass) { this.answerClass = answerClass; }
    public void setAnswerTtl(int answerTtl) { this.answerTtl = answerTtl; }
    public void setAnswerData(String answerData) { this.answerData = answerData; }

    private String readDomainName(ByteBuffer buffer) {
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

    private void writeDomainName(ByteBuffer buffer, String name) {
        String[] labels = name.split("\\.");
        for (String label : labels) {
            buffer.put((byte) label.length());
            buffer.put(label.getBytes());
        }
        buffer.put((byte) 0);
    }
}
