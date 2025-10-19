package common;

import java.util.ArrayList;
import java.util.List;


// Основной класс DNS сообщения
public class DnsMessage {
    private int id;
    private DnsMessageType messageType;
    private DnsOpCode opCode;
    private boolean authoritativeAnswer;
    private boolean truncated;
    private boolean recursionDesired;
    private boolean recursionAvailable;
    private DnsResponseCode responseCode;

    private List<DnsQuestion> questions = new ArrayList<>();
    private List<DnsResourceRecord> answers = new ArrayList<>();
    private List<DnsResourceRecord> authorityRecords = new ArrayList<>();
    private List<DnsResourceRecord> additionalRecords = new ArrayList<>();

    // Конструктор для создания нового сообщения
    public DnsMessage(int id, DnsMessageType messageType, DnsOpCode opCode) {
        this.id = id;
        this.messageType = messageType;
        this.opCode = opCode;
    }

    // Getters и Setters
    public int getId() { return id; }
    public DnsMessageType getMessageType() { return messageType; }
    public DnsOpCode getOpCode() { return opCode; }
    public boolean isAuthoritativeAnswer() { return authoritativeAnswer; }
    public boolean isTruncated() { return truncated; }
    public boolean isRecursionDesired() { return recursionDesired; }
    public boolean isRecursionAvailable() { return recursionAvailable; }
    public DnsResponseCode getResponseCode() { return responseCode; }

    public List<DnsQuestion> getQuestions() { return questions; }
    public List<DnsResourceRecord> getAnswers() { return answers; }
    public List<DnsResourceRecord> getAuthorityRecords() { return authorityRecords; }
    public List<DnsResourceRecord> getAdditionalRecords() { return additionalRecords; }

    public void setAuthoritativeAnswer(boolean authoritativeAnswer) {
        this.authoritativeAnswer = authoritativeAnswer;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public void setRecursionDesired(boolean recursionDesired) {
        this.recursionDesired = recursionDesired;
    }

    public void setRecursionAvailable(boolean recursionAvailable) {
        this.recursionAvailable = recursionAvailable;
    }

    public void setResponseCode(DnsResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public void addQuestion(DnsQuestion question) {
        questions.add(question);
    }

    public void addAnswer(DnsResourceRecord answer) {
        answers.add(answer);
    }

    public void addAuthorityRecord(DnsResourceRecord record) {
        authorityRecords.add(record);
    }

    public void addAdditionalRecord(DnsResourceRecord record) {
        additionalRecords.add(record);
    }
}
