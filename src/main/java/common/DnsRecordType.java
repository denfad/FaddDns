package common;

public enum DnsRecordType {
    A(1),
    NS(2),
    CNAME(5),
    SOA(6),
    PTR(12),
    MX(15),
    TXT(16),
    AAAA(28);

    private final int value;

    DnsRecordType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DnsRecordType fromValue(int value) {
        for (DnsRecordType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DNS record type: " + value);
    }
}
