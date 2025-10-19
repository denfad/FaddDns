package common;

// Класс для DNS вопроса
public class DnsQuestion {
    private String name;
    private DnsRecordType type;
    private DnsRecordClass recordClass;

    public DnsQuestion(String name, DnsRecordType type, DnsRecordClass recordClass) {
        this.name = name;
        this.type = type;
        this.recordClass = recordClass;
    }

    // Getters
    public String getName() { return name; }
    public DnsRecordType getType() { return type; }
    public DnsRecordClass getRecordClass() { return recordClass; }
}
