package common;

// Класс для DNS ресурсной записи
public class DnsResourceRecord {
    private String name;
    private DnsRecordType type;
    private DnsRecordClass recordClass;
    private long ttl;
    private byte[] data;

    public DnsResourceRecord(String name,
                             DnsRecordType type,
                             DnsRecordClass recordClass,
                             long ttl,
                             byte[] data) {
        this.name = name;
        this.type = type;
        this.recordClass = recordClass;
        this.ttl = ttl;
        this.data = data;
    }

    // Getters
    public String getName() { return name; }
    public DnsRecordType getType() { return type; }
    public DnsRecordClass getRecordClass() { return recordClass; }
    public long getTtl() { return ttl; }
    public byte[] getData() { return data; }
}
